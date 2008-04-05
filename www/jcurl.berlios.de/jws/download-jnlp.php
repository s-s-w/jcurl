<?php
#
# Inspired by http://forum.springframework.org/showpost.php?p=121432&postcount=5
#
$file = $_SERVER["PATH_INFO"];
#NOTE: Check for directory traversal attacks here
$file_real=$_SERVER["DOCUMENT_ROOT"] . $file;

if (substr($file, 0, 1) == '.' || strpos($file, '..') > 0){
        header("HTTP/1.0 403 Forbidden");
        echo "You cannot access other directories. IP logged.";
        exit();
}
if (file_exists($file_real)) {
        // Get extension of requested file
        $extension = strtolower(substr(strrchr($file, "."), 1));
        // Determine correct MIME type
        switch($extension) {
                case "jnlp": process_jnlp($file, $file_real); break;
                case "jar": process_jar($file, $file_real); break;
                default:
                        header("HTTP/1.0 403 Forbidden");
                        echo("unknown file type");
                        break;
        }
} else {
        echo "File not found\n";
}

function process_jnlp($file, $file_real) {
        $slash = strrpos($file, "/") + 1;
        $name = substr($file, $slash);
        $path = substr($file, 0, $slash);
        $codebase = "http://" . $_SERVER["SERVER_NAME"] . $path;

        $log = date("Y/m/d h:i:s", mktime()) . " " . $_SERVER["REQUEST_METHOD"]. " " . $_SERVER['REQUEST_URI'] . " Replacing codebase with $codebase\n";
        error_log($log, 3, "jnlp.log");
        $jnlp = file_get_contents($file_real);
        $jnlp = str_replace('$$name', $name, $jnlp);
        $jnlp = str_replace('$$codebase', $codebase, $jnlp);

        header('Content-Type: application/x-java-jnlp-file');
        header("Content-Disposition: attachment; filename=" . $name . ";");
        header("Content-Transfer-Encoding: binary");
        header("Content-Length: " . strlen($jnlp));

        echo $jnlp;
}

function process_jar($file, $file_real) {
        $pack = 0;
        $accept = explode(",", $_SERVER["HTTP_ACCEPT_ENCODING"]);
        for ($i=0; $i<count($accept); $i++) {
                if (strcmp(trim($accept[$i]), "pack200-gzip") == 0) {
                        $pack = $file_real . ".pack.gz";
                        break;
                }
        }
        if ($pack && file_exists($pack)) {
                header("Content-Encoding: pack200-gzip");
                $file_real = $pack;
        }

        $if_modified_since = preg_replace('/;.*$/', '', $_SERVER["HTTP_IF_MODIFIED_SINCE"]);
        $mtime = filemtime($file_real);
        $gmdate_mod = gmdate('D, d M Y H:i:s', $mtime) . ' GMT';
        if ($if_modified_since == $gmdate_mod) {
                header("HTTP/1.0 304 Not Modified");
                exit;
        }
        header("Last-Modified: $gmdate_mod");
        header('Content-Type: x-java-archive');
        header("Content-Length: " . filesize($file_real));
        header("Content-Transfer-Encoding: binary");
        $log = date("Y/m/d h:i:s", mktime()) . " " . $_SERVER["REQUEST_METHOD"]. " " . $_SERVER['REQUEST_URI'] . " Serving $file_real, dated $gmdate_mod\n";
        error_log($log, 3, "jnlp.log");

        if (strcmp($_SERVER["REQUEST_METHOD"], "GET") == 0) {
                readfile($file_real);
        }
}

?>
