<?php
/** 
 * This script delivers jar files compressed with pack200-gzip (*.jar.pack.gz) or
 * with gzip (*.jar.gz) if available.
 * 
 * Test e.g. calling
 * $ wget -S --header="Accept-Encoding: pack200-gzip"  http://www.jcurl.org/jws/org/jcurl/demo/3rdparty/0.7-SNAPSHOT/commons-logging-1.1.jar
 *
 * This script is based on pack200.php from 
 * http://www.randelshofer.ch/pocketplayer/files/pocketplayer-6.1.25.nested.zip
 * and the pack200 deployment guide:
 * http://java.sun.com/j2se/1.5.0/docs/guide/deployment/deployment-guide/pack200.html
 */

/** 
 * Put this script alongside your *.jar, *.jar.gz and *.jar.pack.gz
 * files and add a .htaccess containing:
 *  # use pack200.php to save bandwidth:
 *  RewriteEngine on
 *  RewriteRule ^(.*)\.jar$ pack200.php/$1.jar.pack.gz
 */

$acceptEncoding = (array_key_exists('HTTP_ACCEPT_ENCODING', $_SERVER)) ? $_SERVER['HTTP_ACCEPT_ENCODING'] : '' ;
$pathInfo = (array_key_exists('PATH_INFO', $_SERVER)) ? $_SERVER['PATH_INFO'] : '';

if (substr($pathInfo, -12) == '.jar.pack.gz') 
  $pathInfo = substr($pathInfo, 0, strlen($pathInfo) - 8);
if (substr($pathInfo, -7) == '.jar.gz') 
  $pathInfo = substr($pathInfo, 0, strlen($pathInfo) - 3);

// For security reason, we only deliver files with the extensions .jar
if (substr($pathInfo, -4) != '.jar') {
	header('HTTP/1.0 404 Not Found');
	exit;
}

// For security reason, we strip all leading points, slashes, and backslashes from pathInfo
$pathInfo = preg_replace('/^[.\/\\\\]/', '', $pathInfo);

// Determine accept encoding
$acceptEncodings = split(',', strtolower($acceptEncoding));
if ($acceptEncoding === false)
	$acceptEncoding = array();

if (in_array('pack200-gzip',$acceptEncodings) && file_exists($pathInfo.'.pack.gz')) {
	deliverFile($pathInfo.'.pack.gz', 'pack200-gzip');
} else if (in_array('gzip',$acceptEncodings) && file_exists($pathInfo.'.gz')) {
	deliverFile($pathInfo.'.gz', 'gzip');
} else {
	deliverFile($pathInfo, null);
}

function deliverFile($file, $contentEncoding) {
	if (file_exists($file) && $filehandle=fopen($file,'r')) {
		$fileCTime = filectime($file);
		
		// We don't need to deliver the file, if it hasn't been modified
		// since the last time it has been requested.
		if (array_key_exists('HTTP_IF_MODIFIED_SINCE', $_SERVER)) {
			$sinceTime = strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']);
			if ($sinceTime !== false && $sinceTime < $fileCTime) {
				header('HTTP/1.0 304 Not Modified');
				exit;
			}
		}
	
		header('Last-Modified: ' . date('r', $fileCTime));
#		header('Debug-Requested-File: ' . $file);
		header('Content-Type: application/x-java-archive');
		header('Content-Length :'.filesize($file));
		if ($contentEncoding != null) {
			header('Content-Encoding: '.$contentEncoding);
		}

		fpassthru($filehandle);
		fclose($filehandle);
	} else {
		header('HTTP/1.0 404 Not Found');
	}
	exit;
}

?>
