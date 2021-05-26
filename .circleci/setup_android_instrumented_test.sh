#!/bin/sh

PROJECT_DIR=~/circleci-caver-java-android

CORE_DIR=$PROJECT_DIR/core
CORE_TEST_DIR=$PROJECT_DIR/core/src/test
CORE_TEST_CAVER_DIR=$CORE_TEST_DIR/java/com/klaytn/caver

ANDROID_DIR=$PROJECT_DIR/android_instrumented_test
ANDROID_TEST_DIR=$ANDROID_DIR/src/androidTest
ANDROID_TEST_CAVER_DIR=$ANDROID_TEST_DIR/java/com/klaytn/caver/android_instrumented_test

mkdir -p $ANDROID_TEST_CAVER_DIR

## To do instrumented tests on Android Emulator, we use test files we already have at `core/src/test/java/com.klaytn.caver/*`.
# 1. Copy unit test files of core to re-use without re-writing same test-cases just for instrumented test.
cp -r $CORE_TEST_CAVER_DIR/base $ANDROID_TEST_CAVER_DIR/
cp -r $CORE_TEST_CAVER_DIR/common $ANDROID_TEST_CAVER_DIR/

# 2. To make instrumented test works, we need to replace some contents.
find  $ANDROID_TEST_CAVER_DIR -type f -name '*.java' | xargs sed -i 's/package com.klaytn.caver/package com.klaytn.caver.android_instrumented_test/g'
find  $ANDROID_TEST_CAVER_DIR -type f -name '*.java' | xargs sed -i 's/import static com.klaytn.caver/import static com.klaytn.caver.android_instrumented_test/g'
find  $ANDROID_TEST_CAVER_DIR -type f -name '*.java' | xargs sed -i 's/import com.klaytn.caver.base/import com.klaytn.caver.android_instrumented_test.base/g'
find  $ANDROID_TEST_CAVER_DIR -type f -name '*.java' | xargs sed -i 's/Caver.DEFAULT_URL/"http:\/\/10.0.2.2:8551"/g'
find  $ANDROID_TEST_CAVER_DIR -type f -name '*.java' | xargs sed -i 's/localhost/10.0.2.2/g'
##