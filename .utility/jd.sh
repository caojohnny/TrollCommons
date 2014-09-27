#!/bin/bash
# This script was originally written by maxiaohao in the aws-mock GitHub project.
# https://github.com/treelogic-swe/aws-mock/

# Set it up
git config --global user.email "woodyc40@gmail.com"
git config --global user.name "AgentTroll"
mkdir gh-pages

# We're gonna make this a proper repo
cd gh-pages
git init

# Lets commit some files
cd ..
mvn clean javadoc:javadoc

# Push!
cd gh-pages
git add .
git commit -m "Auto-publishing Javadoc from Shippable CI"
git push -fq https://AgentTroll:${PASS}@github.com/AgentTroll/TrollCommons.git HEAD:gh-pages
echo "Published JavaDoc.\n" # Done!
