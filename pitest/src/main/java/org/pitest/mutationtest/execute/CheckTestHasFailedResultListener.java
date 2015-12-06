/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.execute;

import org.pitest.functional.Option;
import org.pitest.mutationtest.DetectionStatus;
import org.pitest.testapi.Description;
import org.pitest.testapi.TestListener;
import org.pitest.testapi.TestResult;

import java.util.ArrayList;

public class CheckTestHasFailedResultListener implements TestListener {

  private ArrayList<Description> failingTests = new ArrayList<Description>();
  private Option<Description> lastFailingTest = Option.none();
  private int                 testsRun        = 0;

  @Override
  public void onTestFailure(final TestResult tr) {
    this.failingTests.add(tr.getDescription());
    this.lastFailingTest = Option.some(tr.getDescription());
  }

  @Override
  public void onTestSkipped(final TestResult tr) {

  }

  @Override
  public void onTestStart(final Description d) {
    this.testsRun++;
  }

  @Override
  public void onTestSuccess(final TestResult tr) {

  }

  public DetectionStatus status() {
    if (this.lastFailingTest.hasSome()) {
      return DetectionStatus.KILLED;
    } else {
      return DetectionStatus.SURVIVED;
    }
  }

  public ArrayList<Description> failingTests() {
    return this.failingTests;
  }

  public String failingTestsS() {
    StringBuilder buf = new StringBuilder();
    for (Description d : this.failingTests()) {
      buf.append(d.getQualifiedName());
      buf.append(",");
    }
    return buf.substring(0, buf.length() - 1);
  }

  public Option<Description> lastFailingTest() {
    return this.lastFailingTest;
  }

  public int getNumberOfTestsRun() {
    return this.testsRun;
  }

  @Override
  public void onRunEnd() {

  }

  @Override
  public void onRunStart() {

  }

}
