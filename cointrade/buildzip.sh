sbt dist
scp -i "cointrade.pem" target/universal/cointrade-1.0-SNAPSHOT.zip ubuntu@ec2-52-15-193-203.us-east-2.compute.amazonaws.com:~/
