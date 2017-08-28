1. verify gpg command responds
2. verify git-ssh agent is running
3. verify oss credentials in settings.xml
4. `mvn versions:set -DnewVersion=1.2.3`
5. git tag 1.2.3
6. `mvn clean deploy -DperformRelease=true` (gpg pass is in password store)
7. login to https://oss.sonatype.org/#stagingRepositories
8. find your artifact: comgithubeis
9. close
10. wait for success, if failure, fix, drop and go to step 6
11. release
12. update readme
13. git commit && git push
14. kill gpg process

Links:

 - https://theholyjava.wordpress.com/2010/02/07/releasing-a-project-to-maven-centr/
 - http://central.sonatype.org/pages/apache-maven.html
 - http://central.sonatype.org/pages/releasing-the-deployment.html
 - http://central.sonatype.org/pages/ossrh-guide.html