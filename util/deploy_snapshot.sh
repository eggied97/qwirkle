set -e -u

if [ "$TRAVIS_REPO_SLUG" == "eggied97/qwirkle" ] && \
   [ "$TRAVIS_JDK_VERSION" == "openjdk7" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] && \
   [ "$TRAVIS_BRANCH" == "master" ]; then
  echo "Publishing Maven snapshot..."

  mvn clean source:jar javadoc:jar --settings="util/settings.xml" -DskipTests=true

  echo "Maven snapshot published."
fi