#!/bin/bash

function doTest()
{

echo ""
echo "================================================= $DIR"
echo "================================================= $DIR"
echo "start start start start start start start start start"
date
echo ""

FILE=/home/tbriggs/svm-work/$DIR/valid.txt
VRUNNER=/home/tbriggs/svm-work/$DIR/valid-runner.sh
LOGFILE=/home/tbriggs/svm-work/results.txt

JOPTS="-Xmx500M -Dresult.dataSet=$DIR -Dresult.outFile=$LOGFILE -Dresult.testName=$TEST"

GANUMPOP=200
GANUMGEN=40
RANDOM=100
HCRESTARTS=20

case "$TEST" in
    random)
       nice java $JOPTS random/Random $FILE $RANDOM
       ;;
    gaalign) 
       nice java $JOPTS evolutionaryAlgorithm/EvolverAlignment $FILE
       ;;
    gaaccuracyshort)
	nice java $RESULT \
		evolutionaryAlgorithm/EvolverAccuracy 2 1 $VRUNNER
       ;;
    gaaccuracy)
       nice java $JOPTS	 \
       	evolutionaryAlgorithm/EvolverAccuracy $GANUMPOP $GANUMGEN $VRUNNER
       ;;
    hill)
       nice java hillClimbing/HillClimber $FILE $HCRESTARTS
       ;;
    hillAccuracy)
       nice java $JOPTS  \
       	   hillClimbing/HillClimberAccuracy  $VRUNNER ~/svm-work/$DIR/ga-train.txt $HCRESTARTS
       ;;
    brutePoly)
		nice java bruteForce/Runner $FILE 1
		;;
    bruteRBF)
		nice java bruteForce/Runner $FILE 2
		;;
    brutePolyAcc)
		nice java $JOPTS  bruteForce/RunnerAccuracy $VRUNNER 1
		;;
    bruteRBFAcc)
		nice java $JOPTS  bruteForce/RunnerAccuracy $VRUNNER 2
		;;
    correlation)
        nice java runSVM/Runner 500 $DIR > ~/svm-work/align/$DIR-2.txt
	;;
    *)
	echo "Error unknown test $TEST"
	;;
esac

echo ""
date
echo "end end end end end end end end end end end end end"
echo "================================================= $DIR"
echo "================================================= $DIR"
echo ""
echo ""

}

# Test is one of : random gaalign gaacuracy hill hillAccuracy brutePoly bruteRBF
#TESTS="gaalign hill brutePoly bruteRBF correlation"

# - just the accuracy tests
#TESTS="hillAccuracy brutePolyAcc bruteRBFAcc gaaccuracy"
#TESTS=gaaccuracy
TESTS=correlation

#DIRS="breast credit diabetes heart ionosphere pima-indians thyroid voting"
DIRS="credit diabetes heart ionosphere pima-indians thyroid voting"
#DIRS="diabetes heart ionosphere thyroid"
#DIRS="
#DIRS="thyroid"

date
cd ~/workspace/CompositeKernel

for TEST in `echo $TESTS`;
do

  echo "Running $TEST"
  date

  for DIR in `echo $*`; 
    do 
    export DIR
    doTest;  
  done
done

date

