#!/bin/bash
# This script was originally written by maxiaohao in the aws-mock GitHub project.
# https://github.com/treelogic-swe/aws-mock/

# Makes sure it is not a PR
if [ "$TRAVIS_PULL_REQUEST" == "false" ]; then
# Set it up
cd $TRAVIS_BUILD_DIR
git config --global user.email "woodyc40@gmail.com"
git config --global user.name "AgentTroll"
mkdir gh-pages

# We're gonna make this a proper repo
cd gh-pages
git init

# Lets commit some files
cd $TRAVIS_BUILD_DIR
mvn clean javadoc:javadoc

# Push!
cd gh-pages
git add .
git commit -m "Auto-publishing on successful travis build $TRAVIS_BUILD_NUMBER"
git push -fq https://${CI_DEPLOY_USERNAME}:${CI_DEPLOY_PASSWORD}@github.com/AgentTroll/BukkitCommons.git HEAD:gh-pages
echo -e "Published JavaDoc.\n" # Done!
fi
