#!/usr/bin/ruby

class DeployWebstart
	def initialize repobase="#{ENV['HOME']}/org.jcurl.www/m2/repo", dstbase="#{ENV['HOME']}/org.jcurl.www/jws"
		@repobase = repobase
		@dstbase = dstbase
	end

    def mkdir dir
    	begin
			Dir.mkdir dir
    	rescue Errno::ENOENT
			mkdir File.dirname(dir)
			Dir.mkdir dir
		rescue Errno::EEXIST
		end
	end

	def deploy groupId, artifactId, version, packaging="zip"
		srcbase = "#{@repobase}/#{groupId.tr '.', '/'}/#{artifactId}/#{version}"
		ver = /^(.+?)(-SNAPSHOT)?$/.match version
		if ver[2]
			# find the most recent entry
			src = Dir["#{srcbase}/#{artifactId}-#{ver[1]}-*-*.#{packaging}"].sort().reverse()[0]
		else
			src = "#{srcbase}/#{artifactId}-#{version}.#{packaging}"
		end
		
		dst = "#{@dstbase}/#{groupId.tr '.', '/'}/#{artifactId}/#{version}"
		mkdir dst
		system "rm -rf #{dst}/*" 
		system "unzip #{src} -d #{dst}" 
    end
end

o = DeployWebstart.new "/home/m/.m2/repository", "/home/m/tmp"
o = DeployWebstart.new

o.deploy "org.jcurl.demo", "tactics", "0.7-SNAPSHOT"
o.deploy "org.jcurl.demo", "tactics", "0.7.3"
o.deploy "org.jcurl.demo", "viewer", "0.7-SNAPSHOT"
o.deploy "org.jcurl.demo", "viewer", "0.7.3"
