set -e -u

if [ "$TRAVIS_REPO_SLUG" == "eggied97/qwirkle" ] && \
   [ "$TRAVIS_JDK_VERSION" == "openjdk7" ] && \
   [ "$TRAVIS_PULL_REQUEST" == "false" ] && \
   [ "$TRAVIS_BRANCH" == "master" ];  then
  echo "Publishing Javadoc and JDiff..."

  cd $HOME
  git clone -q -b gh-pages https://${GH_TOKEN}@github.com/eggied97/qwirkle gh-pages > /dev/null
  cd gh-pages

  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"

  ./updaterelease.sh snapshot

  git push -fq origin gh-pages > /dev/null

  echo "Javadoc published to gh-pages."
fi