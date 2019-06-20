#!/bin/bash
echo
echo "Standard Commands:"
echo " update                         Updates database to current version"
echo " rollback <tag>                 Rolls back the database to the the state is was"
echo "                                when the tag was applied"


echo -n "Enter command (rollback <tag-name>, update) > "
read command
echo "You entered: $command"

java -cp "liquibase-core-3.6.3.jar;lib/*" liquibase.integration.commandline.Main $command
