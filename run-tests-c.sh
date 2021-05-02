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
       java $JOPTS random/Random $FILE $RANDOM
       ;;
    gaalign) 
       java $JOPTS evolutionaryAlgorithm/EvolverAlignment $FILE
       ;;
    gaaccuracyshort)
	java $RESULT \
		evolutionaryAlgorithm/EvolverAccuracy 2 1 $VRUNNER
       ;;
    gaaccuracy)
       java $JOPTS	 \
       	evolutionaryAlgorithm/EvolverAccuracy $GANUMPOP $GANUMGEN $VRUNNER
       ;;
    hill)
       java hillClimbing/HillClimber $FILE $HCRESTARTS
       ;;
    hillAccuracy)
       java $JOPTS  \
       	   hillClimbing/HillClimberAccuracy  $VRUNNER ~/svm-work/$DIR/ga-train.txt $HCRESTARTS
	;;	
    hillAccuracyPoly)
       java $JOPTS  \
       	   hillClimbing/HillClimberAccuracyPoly  $VRUNNER ~/svm-work/$DIR/ga-train.txt $HCRESTARTS
	;;	
    hillAccuracyRBF)
       java $JOPTS  \
       	   hillClimbing/HillClimberAccuracyRBF  $VRUNNER ~/svm-work/$DIR/ga-train.txt $HCRESTARTS
	;;	
    hillFeature)
       java $JOPTS  \
       	   hillClimbing/HillClimberFeatures  $VRUNNER ~/svm-work/$DIR/ga-train.txt $HCRESTARTS 11
       ;;
    brutePoly)
		java bruteForce/Runner $FILE 1
		;;
    bruteRBF)
		java bruteForce/Runner $FILE 2
		;;
    brutePolyAcc)
		java $JOPTS  bruteForce/RunnerAccuracy $VRUNNER 1
		;;
    bruteRBFAcc)
		java $JOPTS  bruteForce/RunnerAccuracy $VRUNNER 2
		;;
    correlation)
        java runSVM/Runner 500 $DIR | tee ~/svm-work/align/$DIR.txt
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
#TESTS=brutePolyAcc
#TESTS=hillFeature
#TESTS=correlation
TESTS="hillAccuracyPoly"

#DIRS="breast credit diabetes heart ionosphere pima-indians thyroid voting"
#DIRS="credit diabetes heart ionosphere pima-indians thyroid voting"
#DIRS="diabetes heart ionosphere thyroid"
#DIRS="diabetes ionosphere thyroid voting"
#DIRS="thyroid"
#DIRS="pima-indians2 pima-indians3"
#DIRS=breast
DIRS=r-thyroid
#DIRS="r-german r-splice r-thyroid r-titanic"

date

for TEST in `echo $TESTS`;
do

  echo "Running $TEST"
  date

  for DIR in `echo $DIRS`; 
    do 
    export DIR
    doTest;  
  done
done

date
