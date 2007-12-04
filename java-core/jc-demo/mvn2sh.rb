#!/usr/bin/ruby -w

class Maven2Startscript

	def run argv, dst
		pom = "#{Dir.getwd}/pom.xml"
		$stderr.puts pom
		deps = []
		pat = /.*\s+([^ :]+):([^ :]+):([^ :]+):([^ :]+) \(scope = ([^)]+)\)/
		IO.popen("mvn --file #{pom} dependency:resolve") do |f|
			f.each_line do |l|
				m = pat.match(l)
				next if m.nil?
				deps << {:groupId => m[1], :artifactId => m[2], :packaging => m[3], :version => m[4], :scope => m[5]}
			end
		end
		deps2shell deps, dst
	end

	def deps2shell deps, dst
		path = [];
		dst.puts '#!/bin/sh'
		path = []
		deps.each do |v|
			next if "compile" != v[:scope]
			path << ("#{ENV['HOME']}/.m2/repository/" << v[:groupId].gsub('.', '/') << "/#{v[:artifactId]}/#{v[:version]}/#{v[:artifactId]}-#{v[:version]}.#{v[:packaging]}")
		end
		dst.puts "cp=#{path.join("\ncp=$cp:")}"
		dst.puts "#cp=$cp: add the binary istself"
		dst.puts "java -cp $cp myClass"
	end
end

Maven2Startscript.new.run(ARGV, $stdout)
