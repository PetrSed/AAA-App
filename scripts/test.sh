#!/bin/sh
total=0
success=0

printing() {
  echo "Еxpected: $2, Received: $1"
  if [ "$1" -eq "$2" ]; then
    echo "Passed \033[0m"
    return 1
  else
    echo "Failed \033[0m"
    return 0
  fi
}

testing() {
  echo "$1:"
  echo "Input: $2"
  echo "Expected result: $3"
  echo "Result:"
  $2 >&2
  printing $? "$4"
  return $?
}

echo "Simplest scenarios"
echo "------------------------------------------------------------"
testing "Test 1.1" "./run.sh" "Manual and exit code 1" 1
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 1.2" "./run.sh -h" "Manual and exit code 1" 1
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 1.3" "./run.sh -q" "Manual and exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
echo
echo "Authentication"
echo
echo "------------------------------------------------------------"
testing "Test 2.1" "./run.sh -login vasya -pass 123" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.2" "./run.sh -login VASYA -pass 123" "exit code 2" 2
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.3" "./run.sh -login VASYA -pass 1234" "exit code 2" 2
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.4" "./run.sh -login asd -pass asd" "exit code 3" 3
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.5" "./run.sh -pass admin -login admin" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.6" "./run.sh -pass 123 -login vasya" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 2.7" "./run.sh -login admin -pass adn" "exit code 4" 4
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
echo
echo "Authorization"
echo
echo "------------------------------------------------------------"
testing "Test 3.1" "./run.sh -login vasya -pass 123 -role READ -res A" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.2" "./run.sh -login vasya -pass 123 -role READ -res A -h" "Manual and exit code 1" 1
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
echo "Test 3.3"
testing "Test 3.3" "./run.sh -login vasya -pass 123 -role WRITE -res A -h" "Manual and exit code 1" 1
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.4" "./run.sh -login vasya -pass 123 -role WRITE -res A" "Manual and exit code 6" 6
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.5" "./run.sh -login vasya -pass 123 -role DELETE -res AA" "exit code 5" 5
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.6" "./run.sh -pass admin -login admin -role EXECUTE -res A.B" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.7" "./run.sh -pass admin -login admin -role EXECUTE -res A.B.C" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.8" "./run.sh -pass admin -login admin -role EXECUTE -res A.D.S" "exit code 6" 6
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 3.9" "./run.sh -pass admin -login admin -role EXECUTE -res A" "exit code 6" 6
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
echo
echo "Accounting"
echo
echo "------------------------------------------------------------"
testing "Test 4.1" "./run.sh -login vasya -pass 123 -role READ -res A -ds 2020-03-12 -de 2020-03-13 -vol 10" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 4.2" "./run.sh -login vasya -pass 123 -role WRITE -res A -ds 2020-03-12 -de 2020-03-13 -vol 10" "exit code 6" 6
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 4.3" "./run.sh -login vasya -pass 123 -role READ -res A -ds 2020-03-13 -de 2020-03-12 -vol 10" "exit code 7" 7
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 4.4" "./run.sh -login vasya -pass 123 -role READ -res A -ds 2020/03/12 -de 2020/03/13 -vol 10" "exit code 7" 7
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 4.5" "./run.sh -login vasya -pass 123 -role READ -res A -ds 2020.03.12 -de 2020.03.13 -vol 10" "exit code 7" 7
success=$(( success + $? ))
total=$(( total + 1))
echo "------------------------------------------------------------"
testing "Test 4.6" "./run.sh -pass admin -login admin -role EXECUTE -res A.B -ds 2020-03-12 -de 2020-03-13 -vol 10" "exit code 0" 0
success=$(( success + $? ))
total=$(( total + 1))

echo

if [ $success -eq $total ]; then
  echo "Result: $success/$total\033[0m tests passed"
  return 0
else
  echo "Result: $success/$total\033[0m tests passed"
  return 1
fi
$SHELL
