#!/usr/bin/awk -f

BEGIN {
	"date --iso-8601=seconds" | getline date;
	print "<html><head><title>Ftp Mirror Status</title></head><body>";
	print "<h1>Ftp Mirror Status</h1>";
	print "<p>The content here and below is mirrored hourly from <a href='http://jcurl.berlios.de/m2'>http://jcurl.berlios.de/m2</a></p>";
	print "<p><b>All the content here is read-only and may be changed at berlios only!</b></p>";
	print "<p>Last update " date "</p>";
	print "<h2>Terminal Output</h2><pre>";
}

/: File exists/	{ next; }
/ Entering Extended Passive Mode /	{ next; }
/^local: /	{ next; }
/ Transfer complete$/	{ next; }
/ jcurl-maven/	{ next; }

/mkdir/ {
	directories[$0] = "new";
	next;
}

/Opening BINARY mode data connection for / {
	currentfile = $8;
	modes[currentfile] = $3;
	speeds[currentfile] = "Failed";
	sizes[currentfile] = "-";
	times[currentfile] = "-";
	next;
}	
/ bytes sent in / {
	sizes[currentfile] = $1 " " $2;
	speeds[currentfile] = $6 " " $7;
	times[currentfile] = $5;
	next;
}	

{
	print $0;
}

END {
	print "</pre><h2>Created Directories</h2><p>";
	for (dir in directories)
		print dir, "<br />";
	print "</p>";

	print "<h2>Uploaded Files</h2><p><table>";
	for (file in modes) {
		print "<tr><td>" modes[file] "</td><td>" times[file] "</td><td>" speeds[file] "</td><td>" sizes[file] "</td><td>" file "</td></tr>";
	}
	print "</table></p>";

	print "</body></html>";
}
