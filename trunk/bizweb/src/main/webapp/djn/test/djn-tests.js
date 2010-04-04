/*
 * Copyright © 2008, 2009 Pedro Agulló Soliveres.
 * 
 * This file is part of DirectJNgine.
 *
 * DirectJNgine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * Commercial use is permitted to the extent that the code/component(s)
 * do NOT become part of another Open Source or Commercially developed
 * licensed development library or toolkit without explicit permission.
 *
 * DirectJNgine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DirectJNgine.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This software uses the ExtJs library (http://extjs.com), which is 
 * distributed under the GPL v3 license (see http://extjs.com/license).
 */

var JAVA_MAX_BYTE = 127;
var JAVA_MIN_BYTE = -128;
var JAVA_MAX_SHORT = 32767;
var JAVA_MIN_SHORT = -32768;
var JAVA_MAX_INT = 2147483647;
var JAVA_MIN_INT = -2147483648;
var JAVA_MAX_LONG = 92233720368547775807;
var JAVA_MIN_LONG = -922337203685477758078;
var JAVA_MAX_FLOAT = 3.4028235e+38;
var JAVA_MIN_FLOAT = 1.4e-45;
var JAVA_MAX_DOUBLE = 1.7976931348623157e+308;
var JAVA_MIN_DOUBLE = 4.9e-324;

Djn.ClientCallErrorTest = {
  testClassName : 'ClientCallErrorTest',
  
  test_serverReceivingFunctionInParameter : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingFunctionInParameter( function () {},
      function(provider, response) {
        Djn.Test.fail( "test_serverReceivingFunctionInParameter", "Expected a Client-side error" );
      });
      Djn.Test.fail( "test_serverReceivingFunctionInParameter", "Expected a Client-side error" );
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingFunctionInParameter", e );    
    }
  },
  
  test_serverReceivingUniqueParameterUndefined : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingUniqueParameterUndefined( undefined,
      function(provider, response) {
        Djn.Test.fail( "test_serverReceivingUniqueParameterUndefined", "Expected a Client-side error" );
      });
      Djn.Test.fail( "test_serverReceivingUniqueParameterUndefined", "Expected a Client-side error" );
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingUniqueParameterUndefined", e );    
    }
  },

  test_serverReceivingSeveralParametersWithTheLastOneUndefined : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingSeveralParametersWithTheLastOneUndefined('a', 'b', undefined, function(provider, response){
        Djn.Test.fail("test_serverReceivingSeveralParametersWithTheLastOneUndefined", "Expected a Client-side error");
      });
      Djn.Test.fail( "test_serverReceivingSeveralParametersWithTheLastOneUndefined", "Expected a Client-side error" );
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingSeveralParametersWithTheLastOneUndefined", e );    
    }
  },

  test_serverReceivingSeveralParametersOneOfThemUndefined : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingSeveralParametersOneOfThemUndefined( 'a', undefined, 'b',
        function(provider, response) {
          Djn.Test.fail( "test_serverReceivingSeveralParametersOneOfThemUndefined", "Expected a Client-side error" );
        }
      );
      Djn.Test.fail( "test_serverReceivingSeveralParametersOneOfThemUndefined", "Expected a Client-side error" );
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingSeveralParametersOneOfThemUndefined", e );    
    }
  },

  test_serverReceivingMoreParametersThanExpected : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingMoreParametersThanExpected(22, 10, function(provider, response){
        Djn.Test.fail( "test_serverReceivingMoreParametersThanExpected", "Expected a Client-side error" );
      });
      Djn.Test.fail( "test_serverReceivingMoreParametersThanExpected", "Expected a Client-side error" );
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingMoreParametersThanExpected", e );    
    }
  },

  test_serverReceivingLessParametersThanExpected : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingLessParametersThanExpected(1, function(provider, response){
        Djn.Test.fail("test_serverReceivingLessParametersThanExpected", "Expected a Client-side error");
      });
      Djn.Test.fail("test_serverReceivingLessParametersThanExpected", "Expected a Client-side error");
    }
    catch( e ) {
      Djn.Test.checkClientCallError( "test_serverReceivingLessParametersThanExpected", e );    
    }
  },

  test_serverReceivingNonEmptyStringArrayHavingAnUndefinedValue: function(){
    try {
      var values = ['value1', undefined, 'value3'];
      ServerMethodParametersReceptionTest.test_serverReceivingNonEmptyStringArrayHavingAnUndefinedValue(values, function(provider, response){
        Djn.Test.fail("test_serverReceivingNonEmptyStringArrayHavingAnUndefinedValue", "Expected a Client-level error");
      });
      Djn.Test.fail("test_serverReceivingNonEmptyStringArrayHavingAnUndefinedValue", "Expected a Client-level error");
    } 
    catch (e) {
      Djn.Test.checkClientCallError("test_serverReceivingNonEmptyStringArrayHavingAnUndefinedValue", e);
    }
  },
  
  test_serverReceivingParametersWithObjectHavingArrayWithUndefinedValue: function(){
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingParametersWithObjectHavingArrayWithUndefinedValue({
        v1: 1,
        v2: ["3", undefined]
      }, function(provider, response){
        Djn.Test.fail("test_serverReceivingParametersWithObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
      });
      Djn.Test.fail("test_serverReceivingParametersWithObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
    } 
    catch (e) {
      Djn.Test.checkClientCallError("test_serverReceivingParametersWithObjectHavingArrayWithUndefinedValue", e);
    }
  },
   
  test_serverReceivingParametersWithArrayHavingArrayWithUndefinedValue: function(){
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingParametersWithArrayHavingArrayWithUndefinedValue([1, ["2", undefined, 3], 2], function(provider, response){
        Djn.Test.fail("test_serverReceivingParametersWithArrayHavingArrayWithUndefinedValue", "Expected a Client-level error");
      });
      Djn.Test.fail("test_serverReceivingParametersWithArrayHavingArrayWithUndefinedValue", "Expected a Client-level error");
    } 
    catch (e) {
      Djn.Test.checkClientCallError("test_serverReceivingParametersWithArrayHavingArrayWithUndefinedValue", e);
    }
  },
   
  test_serverReceivingParametersWithArrayHavingObjectHavingArrayWithUndefinedValue: function(){
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingParametersWithArrayHavingObjectHavingArrayWithUndefinedValue([1, {
        v1: '2',
        v2: [5, undefined, 55]
      }, 2], function(provider, response){
        Djn.Test.fail("test_serverReceivingParametersWithArrayHavingObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
      });
      Djn.Test.fail("test_serverReceivingParametersWithArrayHavingObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
    } 
    catch (e) {
      Djn.Test.checkClientCallError("test_serverReceivingParametersWithArrayHavingObjectHavingArrayWithUndefinedValue", e);
    }
  },
    
  test_serverReceivingParametersWithObjectHavingObjectHavingArrayWithUndefinedValue: function(){
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingParametersWithObjectHavingObjectHavingArrayWithUndefinedValue({
        v1: '2',
        v2: {
          v3: 88,
          v4: [5, undefined, 55]
        }
      }, function(provider, response){
        Djn.Test.fail("test_serverReceivingParametersWithObjectHavingObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
      });
      Djn.Test.fail("test_serverReceivingParametersWithObjectHavingObjectHavingArrayWithUndefinedValue", "Expected a Client-level error");
    } 
    catch (e) {
      Djn.Test.checkClientCallError("test_serverReceivingParametersWithObjectHavingObjectHavingArrayWithUndefinedValue", e);
    }
  },

  test_serverReceivingUndefinedForAPrimitiveArgument : function() {
    try {
      ServerMethodParametersReceptionTest.test_serverReceivingUndefinedForAPrimitiveArgument(undefined, function(provider, response){
        Djn.Test.fail( "test_serverReceivingUndefinedForAPrimitiveArgument" );
      });
      Djn.Test.fail( "test_serverReceivingUndefinedForAPrimitiveArgument" );
    }
    catch( e ) {    
      Djn.Test.checkClientCallError( "test_serverReceivingUndefinedForAPrimitiveArgument", e );    
    }
  },
  
  test_serverRecevingLongStringParameters : function() {
    try {
      var param1 = '';
      for( i = 0; i < 1000; i++ ) {
        param1 += 'a';
      }
      var param2 = "\n\r\u0050";
      for( i = 0; i < 10000; i++ ) {
        param2 += 'b';
      }
      ServerMethodParametersReceptionTest.test_serverRecevingLongStringParameters(param1, param2, function(provider, response){
        Djn.Test.checkSuccessfulResponse( "test_serverRecevingLongStringParameters", response, response.result === param1 + param2, response.result);
      });
    }
    catch( e ) {    
      Djn.Test.checkClientCallError( "test_serverReceivingUndefinedForAPrimitiveArgument", e );    
    }
  },
  
  test_serverProblematicLongString2 : function() {
    try {
      var param1 = "symphony";
      var param2 = " select * from `scrapbucketout` where parent='beb8596a-1dc0-4e61-9297-3267ab5d1528' and type='file'";
      var param3 = 5;
      var param4 = 5;
      var param5 = "rO0ABXNyACdjb20uYW1hem9uLnNkcy5RdWVyeVByb2Nlc3Nvci5Nb3JlVG9rZW7racXLnINNqwMA C0kAFGluaXRpYWxDb25qdW5jdEluZGV4WgAOaXNQYWdlQm91bmRhcnlKAAxsYXN0RW50aXR5SURa AApscnFFbmFibGVkSQAPcXVlcnlDb21wbGV4aXR5SgATcXVlcnlTdHJpbmdDaGVja3N1bUkACnVu aW9uSW5kZXhaAA11c2VRdWVyeUluZGV4TAANY29uc2lzdGVudExTTnQAEkxqYXZhL2xhbmcvU3Ry aW5nO0wAEmxhc3RBdHRyaWJ1dGVWYWx1ZXEAfgABTAAJc29ydE9yZGVydAAvTGNvbS9hbWF6b24v c2RzL1F1ZXJ5UHJvY2Vzc29yL1F1ZXJ5JFNvcnRPcmRlcjt4cAAAAAQATw0PyGR0wAAAAAAAAQAA AACnEObjAAAAAAFwdAAkYmViODU5NmEtMWRjMC00ZTYxLTkyOTctMzI2N2FiNWQxNTI4fnIALWNv bS5hbWF6b24uc2RzLlF1ZXJ5UHJvY2Vzc29yLlF1ZXJ5JFNvcnRPcmRlcgAAAAAAAAAAEgAAeHIA DmphdmEubGFuZy5FbnVtAAAAAAAAAAASAAB4cHQACUFTQ0VORElOR3g=";
      ServerMethodParametersReceptionTest.test_serverProblematicLongString2(param1, param2, param3, param4, param5, function(provider, response){
        var expectedResult = param1 + param2 + param5;
        Djn.Test.checkSuccessfulResponse( "test_serverProblematicLongString2", response, response.result === expectedResult, response.result);
      });
    }
    catch( e ) {    
      Djn.Test.checkClientCallError( "test_serverProblematicLongString2", e );    
    }
  }
  
  

//  test_serverReceivingCallCausingInfiniteRecursionIfParametersRecursivelyChecked :function() {
//    try {
//      var param1 = {};
//      var param2 = {
//        other : param1
//      };
//      param1.other = param2;
//      
//      ServerMethodParametersReceptionTest.test_serverReceivingCallCausingInfiniteRecursionIfParametersRecursivelyChecked(param1, function(provider, response){
//        Djn.Test.fail( "test_serverReceivingCallCausingInfiniteRecursionIfParametersRecursivelyChecked" );
//      });
//      Djn.Test.fail( "test_serverReceivingCallCausingInfiniteRecursionIfParametersRecursivelyChecked" );
//    }
//    catch( e ) {    
//      Djn.Test.checkClientCallError( "test_serverReceivingCallCausingInfiniteRecursionIfParametersRecursivelyChecked", e );    
//    }
//  }
  
}

Djn.ServerMethodReturnTest = {
  testClassName : 'ServerMethodReturnTest',
  
  test_chineseStringsWorkCorrectly : function() {
    ServerMethodReturnTest.test_chineseStringsWorkCorrectly( '界世界',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_chineseStringsWorkCorrectly", response, response.result === '世界', 'Expected: ' + response.result);
      }
    );
  },
  
  test_serverReturningMap : function() {
    ServerMethodReturnTest.test_serverReturningMap(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningMap", response, response.result.key1 === 'value1' && response.result.key2 === null, response.result);
      }
    );    
  },
  
  test_serverReturningNothing : function() {
    ServerMethodReturnTest.test_serverReturningNothing(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningNothing", response, response.result === null, response.result);
      }
    );
  },
  
  test_serverReturningNull : function() {
    // A method that returns nothing in Java (void), returns 'null', not 'undefined' in ExtJs Direct
    ServerMethodReturnTest.test_serverReturningNull(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningNull", response, response.result === null, response.result);
      }
    );
  },  
  
  test_serverReturningByte : function() {
    ServerMethodReturnTest.test_serverReturningByte(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningByte", response, response.result === JAVA_MAX_BYTE, response.result);
      }
    );
  },  

  test_serverReturningByteObject : function() {
    ServerMethodReturnTest.test_serverReturningByteObject(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningByteObject", response, response.result === JAVA_MIN_BYTE, response.result);
      }
    );
  },  
  
  test_serverReturningShort : function() {
    ServerMethodReturnTest.test_serverReturningShort(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningShort", response, response.result === JAVA_MAX_SHORT, response.result);
      }
    );
  },  
  
  test_serverReturningShortObject : function() {
    ServerMethodReturnTest.test_serverReturningShortObject(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningShortObject", response, response.result === JAVA_MIN_SHORT, response.result);
      }
    );
  },  
  
  test_serverReturningInt : function() {
    ServerMethodReturnTest.test_serverReturningInt(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningInt", response, response.result === JAVA_MAX_INT, response.result);
      }
    );
  },  
  
  test_serverReturningIntegerObject : function() {
    ServerMethodReturnTest.test_serverReturningIntegerObject(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningIntegerObject", response, response.result === JAVA_MIN_INT, response.result);
      }
    );
  },  
  
  test_serverReturningLong : function() {
    ServerMethodReturnTest.test_serverReturningLong(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MAX_LONG;
        // A javascript number is precise up to 15 digits, but Java's Long.MAX_VALUE has many more digits, and therefore JavaScript can't represent it adequately
        Djn.Test.checkSuccessfulResponse( "test_serverReturningLong", response, response.result !== JAVA_MAX_LONG, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningLongObject : function() {
    ServerMethodReturnTest.test_serverReturningLongObject(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MIN_LONG; 
        // A javascript number is precise up to 15 digits, but Java's Long.MIN_VALUE has many more digits, and therefore JavaScript can't represent it adequately
        Djn.Test.checkSuccessfulResponse( "test_serverReturningLongObject", response, response.result !== JAVA_MIN_LONG, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningFloat : function() {
    ServerMethodReturnTest.test_serverReturningFloat(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MAX_FLOAT; 
        Djn.Test.checkSuccessfulResponse( "test_serverReturningFloat", response, response.result === JAVA_MAX_FLOAT, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningFloatObject : function() {
    ServerMethodReturnTest.test_serverReturningFloatObject(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MIN_FLOAT; 
        Djn.Test.checkSuccessfulResponse( "test_serverReturningFloatObject", response, response.result === JAVA_MIN_FLOAT, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningDouble : function() {
    ServerMethodReturnTest.test_serverReturningDouble(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MAX_DOUBLE; 
        Djn.Test.checkSuccessfulResponse( "test_serverReturningDouble", response, response.result === JAVA_MAX_DOUBLE, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningDoubleObject : function() {
    ServerMethodReturnTest.test_serverReturningDoubleObject(
      function(provider, response) {
        var value = response.result;
        var expectedValue = JAVA_MIN_DOUBLE; 
        Djn.Test.checkSuccessfulResponse( "test_serverReturningDoubleObject", response, response.result === JAVA_MIN_DOUBLE, "Received " + value + ", expected " + expectedValue);
      }
    );
  },  
  
  test_serverReturningString : function() {
    ServerMethodReturnTest.test_serverReturningString(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningString", response, response.result === 'abC', response.result);
      }
    );
  },  
  
  test_serverReturningEmptyString : function() {
    ServerMethodReturnTest.test_serverReturningEmptyString(
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningEmptyString", response, response.result === '', response.result);
      }
    );
  },

  test_serverReturningVeryComplexObject : function() {
    ServerMethodReturnTest.test_serverReturningVeryComplexObject(function(provider, response){
      var obj = response.result;
      var ok = obj != null && 
           obj.ints.length == 2 && obj.ints[0] === 33 && obj.ints[1] == null &&
           obj.myComplexObject != null && obj.myComplexObject.name === "MyPet" && obj.myComplexObject.age == 0 &&
           obj.moreComplexObjects != null && obj.moreComplexObjects.length == 2 && 
             obj.moreComplexObjects[0] == null && 
             obj.moreComplexObjects[1] != null && obj.moreComplexObjects[1].name == null && obj.moreComplexObjects[1].age == 5 &&
           obj.notSetInJs == 19; 
      Djn.Test.checkSuccessfulResponse("test_serverReturningVeryComplexObject", response, ok);
    });
  },
  
  test_serverReturningPrimitiveDoubleArray : function() {
    ServerMethodReturnTest.test_serverReturningPrimitiveDoubleArray( 2.5, 2,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReturningPrimitiveDoubleArray", response, response.result.length === 2 && response.result[0] == 2.5 && response.result[1] == 2.5, response.result === '');
      }
    );
  }
}

Djn.ServerMethodParametersReceptionTest = {
  testClassName : 'ServerMethodParametersReceptionTest',
  
//  test_serverReceivingJavascriptDateAsJavaDate : function() {
//    var date = new Date();
//    date.setYear( 1915 );
//    date.setMonth( 5 );
//    date.setDate( 25 );
//    date.setHours(13);
//    date.setMinutes( 55 );
//    date.setSeconds( 28 );
//    ServerMethodParametersReceptionTest.test_serverReceivingJavascriptDateAsJavaDate(date, function(provider, response) {
//      debugger;
//      Djn.Test.checkSuccessfulResponse( "test_serverReceivingJavascriptDateAsJavaDate", response, response.result === true, "Attempt to pass Javascript date to Java Date failed");
//    });
//  },
   
  test_privateMethodCall : function() {
    ServerMethodParametersReceptionTest.test_privateMethodCall(function(provider, response) {
      Djn.Test.checkSuccessfulResponse( "test_privateMethodCall", response, response.result, response.result);
    });
  },
   
  test_privateStaticMethodCall : function() {
    ServerMethodParametersReceptionTest.test_privateStaticMethodCall(function(provider, response) {
      Djn.Test.checkSuccessfulResponse( "test_privateStaticMethodCall", response, response.result, response.result);
    });
  },
   
  test_serverReceivingNoArguments : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingNoArguments(function(provider, response) {
      Djn.Test.checkSuccessfulResponse( "test_serverReceivingNoArguments", response, response.result === 'noArgumentsMethod called', response.result);
    });
  },

  test_serverReceivingOnePrimitiveArgument : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingOnePrimitiveArgument( 99,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingOnePrimitiveArgument", response, response.result === (99+1), response.result);
      }
    );
  },

  test_serverReceivingPrimitiveAndString : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingPrimitiveAndString( 33, 'a_value',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingPrimitiveAndString", response, response.result === 'a_value&&33', response.result);
      }
    );
  },


  test_serverCallForNonAnnotatedMethod : function() {
    ServerMethodParametersReceptionTest.test_serverCallForNonAnnotatedMethod( 33, 'a_value',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverCallForNonAnnotatedMethod", response, response.result === 'a_value&&33', response.result);
      }
    );
  },

  test_serverReceivingParametersOfAllPrimitiveAndWrapperTypesExceptLongCorrectly : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingParametersOfAllPrimitiveAndWrapperTypesExceptLongCorrectly(
        JAVA_MAX_BYTE, JAVA_MAX_SHORT, JAVA_MAX_INT, JAVA_MAX_FLOAT, JAVA_MAX_DOUBLE, true, 'a',
        JAVA_MIN_BYTE, JAVA_MIN_SHORT, JAVA_MIN_INT, JAVA_MIN_FLOAT, JAVA_MIN_DOUBLE, false, 'b', JAVA_MIN_DOUBLE,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingParametersOfAllPrimitiveAndWrapperTypesExceptLongCorrectly", response, response.result === 'ok', response.result);
      }
    );    
  }, 
  
  test_serverReceivingOneNullStringArgument : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingOneNullStringArgument( null,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingOneNullStringArgument", response, response.result);
      }
    );
  },

  test_serverReceivingUniqueParameterNull : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingUniqueParameterNull( null,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingUniqueParameterNull", response, response.result === 'ok', response.result);
      }
    );
  },
  
  test_serverReceivingSeveralParametersWithTheLastOneNull : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingSeveralParametersWithTheLastOneNull( 'a', 'b', null,
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingSeveralParametersWithTheLastOneNull", response, response.result === 'ok', response.result);
      }
    );
  },

  test_serverCallWithNewLineCharacters : function() {
    ServerMethodParametersReceptionTest.test_serverCallWithNewLineCharacters( '\r\n',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverCallWithNewLineCharacters", response, response.result === '\r\n', response.result);
      }
    );
  },

  test_serverCallWithSpecialCharacters : function() {
    ServerMethodParametersReceptionTest.test_serverCallWithSpecialCharacters( '"\\/\b\f\n\r\t', //'a\nb\rc\r\nd',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverCallWithSpecialCharacters", response, response.result === '"\\/\b\f\n\r\t', response.result);
      }
    );
  },

  test_serverCallWithEscapedCharacters : function() {
    ServerMethodParametersReceptionTest.test_serverCallWithEscapedCharacters( '\u0050',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverCallWithEscapedCharacters", response, response.result === '\u0050', response.result);
      }
    );
  },

  test_serverReceivingSeveralParametersOneOfThemNull : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingSeveralParametersOneOfThemNull( 'a', null, 'b',
      function(provider, response) {
        Djn.Test.checkSuccessfulResponse( "test_serverReceivingSeveralParametersOneOfThemNull", response, response.result === 'ok', response.result);
      }
    );
  },

  test_serverReceivingEmptyPrimitiveArray : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingEmptyPrimitiveArray([], function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingEmptyPrimitiveArray", response, response.result, response.result);
    });
  },
  
  test_serverReceivingNullPrimitiveArray : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingNullPrimitiveArray(null, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingNullPrimitiveArray", response, response.result, response.result);
    });
  },
  
  test_serverReceivingEmptyStringArray : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingEmptyStringArray([], function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingEmptyStringArray", response, response.result, response.result);
    });
  },

  test_serverReceivingNullStringArray : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingNullStringArray(null, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingNullStringArray", response, response.result, response.result);
    });
  },

  test_serverReceivingNullForAPrimitiveArgument : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingNullForAPrimitiveArgument(null, function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingNullForAPrimitiveArgument", response, 'IllegalArgumentException');
    });
  },
  
  test_serverReceivingStringForAnIntArgument : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingStringForAnIntArgument('a string', function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingStringForAnIntArgument", response, "IllegalArgumentException");
    });
  },
  
  test_serverReceivingStringRepresentingValidIntForAnIntArgument : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingStringRepresentingValidIntForAnIntArgument('999', function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingStringRepresentingValidIntForAnIntArgument", response, "IllegalArgumentException");
    });
  },
  
  test_serverReceivingCharFromSize1String : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingCharFromSize1String('a', function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingCharFromSize1String", response, response.result === 'a', "Expected to receive 'a', received '" + response.result + "'");
    });
  },

  test_serverReceivingCharFromSize2String : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingCharFromSize2String('ab', function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingCharFromSize2String", response, 'IllegalArgumentException');
    });
  },

  test_serverReceivingCharFromNumberInValidJavaCharRange : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingCharFromNumberInValidJavaCharRange(532, function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingCharFromNumberInValidJavaCharRange", response, 'IllegalArgumentException');
    });
  },

  test_serverReceivingCharFromNumberTooBig : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingCharFromNumberTooBig(55555, function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingCharFromNumberTooBig", response, 'IllegalArgumentException');
    });
  },

  test_serverReceivingCharFromNumberTooSmall : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingCharFromNumberTooSmall(-55555, function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingCharFromNumberTooSmall", response, 'IllegalArgumentException');
    });
  },

  test_serverReceivingByteFromANumberTooBig : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingByteFromANumberTooBig(JAVA_MAX_BYTE+1, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingByteFromANumberTooBig", response, response.result !== JAVA_MAX_BYTE+1, "We expected to receive something different from what we sent, but received '" + response.result + "'");
    });
  },

  test_serverReceivingByteFromANumberTooSmall : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingByteFromANumberTooSmall(JAVA_MIN_BYTE-1, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingByteFromANumberTooSmall", response, response.result !== JAVA_MIN_BYTE-1, "We expected to receive something different from what we sent, but received '" + response.result + "'");
    });
  },

  test_serverReceivingBigDecimal : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingBigDecimal(3.2, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingBigDecimal", response, response.result, "We expected to receive something different from what we sent, but received '" + response.result + "'");
    });
  },

  test_serverReceivingBigDecimalFromString : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingBigDecimalFromString("3.33", function(provider, response){
      // Djn.Test.checkSuccessfulResponse("test_serverReceivingBigDecimalFromString", response, response.result, "We expected to receive something different from what we sent, but received '" + response.result + "'");
      Djn.Test.checkServerErrorResponse("test_serverReceivingBigDecimalFromString", response, 'IllegalArgumentException' );
    });
  },

  test_serverReceivingBigInteger : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingBigInteger(92, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingBigInteger", response, response.result, "We expected to receive something different from what we sent, but received '" + response.result + "'");
    });
  },

  test_serverReceivingBigIntegerFromString : function() {
    ServerMethodParametersReceptionTest.test_serverReceivingBigIntegerFromString("88", function(provider, response){
      // Djn.Test.checkSuccessfulResponse("test_serverReceivingBigIntegerFromString", response, response.result, "We expected to receive something different from what we sent, but received '" + response.result + "'");
      Djn.Test.checkServerErrorResponse("test_serverReceivingBigIntegerFromString", response, 'IllegalArgumentException' );
    });
  },

//  test_serverReceivingJavascriptDate : function() {
//    var date = new Date();
//    date.setYear( 1991 );
//    date.setMonth( 5 );
//    date.setDate( 15 );
//    date.setMinutes( 22 );
//    date.setHours( 12 );
//    date.setSeconds( 18 );
//    date.setMilliseconds( 999 );
//    ServerMethodParametersReceptionTest.test_serverReceivingJavascriptDate( date, function(provider, response){
//      Djn.Test.checkSuccessfulResponse("test_serverReceivingJavascriptDate", response, response.result === date, "We expected to receive something different from what we sent, but received '" + response.result + "'");
//    });
//  },
 
  test_serverReceivingComplexObject : function() {
    var obj = { name: 'MyPet', age : 2, fieldThatDoesNotExistsInJavaClass : 'will not be copied to Java object' };
    ServerMethodParametersReceptionTest.test_serverReceivingComplexObject(obj, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingComplexObject", response, response.result);
    });
  },

  /* Future development?
  test_serverReceivingMap : function() {
    var map = {
      key1 : "value1",
      key2 : null
    }
    ServerMethodParametersReceptionTest.test_serverReceivingMap(map, function(provider, response) {
      Djn.Test.checkSuccessfulResponse( "test_serverReceivingMap", response, response.result === true, response.result);
    });
  }
  */  

  test_serverReceivingNonEmptyPrimitiveArray: function(){
    ServerMethodParametersReceptionTest.test_serverReceivingNonEmptyPrimitiveArray([5, 3], function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingNonEmptyPrimitiveArray", response, response.result, response.result);
    });
  },
  
  test_serverReceivingNonEmptyStringArray: function(){
    ServerMethodParametersReceptionTest.test_serverReceivingNonEmptyStringArray(['value1', 'value2'], function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingNonEmptyStringArray", response, response.result, response.result);
    });
  },
    
  test_serverReceivingNonEmptyStringArrayHavingANullValue: function(){
    ServerMethodParametersReceptionTest.test_serverReceivingNonEmptyStringArrayHavingANullValue(['value1', null, 'value3'], function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingNonEmptyStringArrayHavingANullValue", response, response.result, response.result);
    });
  },
    
  test_serverReceivingOneElementIntArrayForAnIntArgument: function(){
    ServerMethodParametersReceptionTest.test_serverReceivingOneElementIntArrayForAnIntArgument([999], function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingOneElementIntArrayForAnIntArgument", response, "IllegalArgumentException");
    });
  },
    
  test_serverReceivingMultiElementIntArrayForAnIntArgument: function(){
    ServerMethodParametersReceptionTest.test_serverReceivingMultiElementIntArrayForAnIntArgument([30, 40], function(provider, response){
      Djn.Test.checkServerErrorResponse("test_serverReceivingMultiElementIntArrayForAnIntArgument", response, 'IllegalArgumentException');
    });
  },
    
  test_serverReceivingDoubleArray: function(){
    var values = [3.2, 4.5, 6.0];
    ServerMethodParametersReceptionTest.test_serverReceivingDoubleArray(values, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingDoubleArray", response, response.result === 13.7);
    });
  },
    
  test_serverReceivingPrimitiveDoubleArray: function(){
    var values = [3.2, null, 6.0];
    ServerMethodParametersReceptionTest.test_serverReceivingPrimitiveDoubleArray(values, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingPrimitiveDoubleArray", response, response.result === 9.2);
    });
  },

  test_serverReceivingVeryComplexObject: function(){
    var obj = {
      ints: [33, null],
      myComplexObject: {
        name: 'MyPet',
        age: undefined // We expect 'undefined' to be ignored!
      },
      moreComplexObjects: [null, {
        name: null,
        age: 5
      }]
    };
    ServerMethodParametersReceptionTest.test_serverReceivingVeryComplexObject(obj, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_serverReceivingVeryComplexObject", response, response.result);
    });
  },
  
  test_handleJsonDataMethod : function() {
    var param = false;
    ServerMethodParametersReceptionTest.test_handleJsonDataMethod(param, function(provider, response){
      Djn.Test.checkSuccessfulResponse("test_handleJsonDataMethod", response, response.result === param);
    });
  }
}


Djn.DirectStoreTest = {
  testClassName : 'DirectStoreTest',
  
  test_load : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:false,
      root:'',
      directFn: DirectStoreTest.test_load,
      
      idProperty:'id',
      remoteSort:true,
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        load: function(s, records){
          var error = false;
          if (records.length !== 2) 
            error = true;
          else {
            var v1 = records[0].data;
            var v2 = records[1].data;
          
            if (v1.id !== 99 || v1.name !== 'name1') {
              error = true
            }
            if (v2.id !== 100 || v2.name !== 'name2') {
              error = true
            }
          }
          
          Djn.Test.check( "test_load", !error, "Found errors" );
        }
      },                    
      baseParams: {}
    });
    myStore.load({
      params: {}
    });
  },
  
  
  test_load2 : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:false,
      root:'items',
      totalProperty : 'rowCount',
      directFn: DirectStoreTest.test_load2,
      
      idProperty:'id',
      remoteSort:true,
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        load: function(s, records){
          var error = false;
                    
          if (records.length !== 2) 
            error = true;
          else if ( myStore.getTotalCount() != 347 ) {
            error = true;
          }
          else {
            var v1 = records[0].data;
            var v2 = records[1].data;
          
            if (v1.id !== 99 || v1.name !== 'name1') {
              error = true
            }
            if (v2.id !== 100 || v2.name !== 'name2') {
              error = true
            }
          }
          
          Djn.Test.check( "test_load2", !error, "Found errors" );
        }
      },                    
      baseParams: {}
    });
    myStore.load({
    });
  },
  
  test_loadWithArguments : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:false,
      root:'items',
      paramOrder: ['argFromBaseParams', 'argPassedInLoadCall', 'argPassedInBeforeLoadEvent'],
      totalProperty : 'rowCount',
      directFn: DirectStoreTest.test_loadWithArguments,
      
      idProperty:'id',
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        beforeload : function(store, options) {
          options.params.argPassedInBeforeLoadEvent = false
        },
        load: function(s, records){
          Djn.Test.check( "test_loadWithArguments", records.length === 2, "If there is an error, this will never be called: a timeout should happen if there is some error!" );
        }
      },                    
      baseParams: {argFromBaseParams:'aValue'}
    });
    myStore.load({
      params: {
        argPassedInLoadCall: 34
      }
    });
  },
  
  test_loadWithArgumentsWithDirectJsonHandling : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:true,
      root:'items',
      // paramOrder: ['argFromBaseParams', 'argPassedInLoadCall', 'argPassedInBeforeLoadEvent'], // Not needed when paramsAsHash:true
      totalProperty : 'rowCount',
      directFn: DirectStoreTest.test_loadWithArgumentsWithDirectJsonHandling,
      
      idProperty:'id',
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        beforeload : function(store, options) {
          options.params.argPassedInBeforeLoadEvent = false
        },
        load: function(s, records){
          Djn.Test.check( "test_loadWithArgumentsWithDirectJsonHandling", records.length === 2, "If there is an error, this will never be called: a timeout should happen if there is some error!" );
        }
      },                    
      baseParams: {argFromBaseParams:'aValue'}
    });
    myStore.load({
      params: {
        argPassedInLoadCall: 34
      }
    });
  },

  test_simulatePassingDynamicParams : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:false,
      root:'items',
      paramOrder: ['param1', 'param2', 'dynamicParams'],
      totalProperty : 'rowCount',
      directFn: DirectStoreTest.test_simulatePassingDynamicParams,
      
      idProperty:'id',
      remoteSort:true,
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        load: function(s, records){
          Djn.Test.check( "test_simulatePassingDynamicParams", true, "If there is an error, this will never be called: a timeout should happen if there is some error!" );
        }
      },                    
      baseParams: {param1:98}
    });
    myStore.load({
      params: {
        param2: true, 
        dynamicParams : [
          {param:'dyn1', value:'55'}, 
          {param:'dyn2', value:'dyn2Value'}
          // ... 
        ]
      }
    });

  }
/*
  test_paramsAsHashSetToTrue : function() {
    var myStore = new Ext.data.DirectStore( {
      paramsAsHash:true,
      root:'items',
      totalProperty : 'rowCount',
      directFn: DirectStoreTest.test_paramsAsHashSetToTrue,
      
      idProperty:'id',
      remoteSort:true,
      fields: [
        {name: 'id' },
        {name: 'name'}
      ],
      listeners: {
        load: function(s, records){
          debugger;
          var error = false;
                    
          if (records.length !== 2) 
            error = true;
          else if ( myStore.getTotalCount() != 347 ) {
            error = true;
          }
          else {
            var v1 = records[0].data;
            var v2 = records[1].data;
          
            if (v1.id !== 99 || v1.name !== 'name1') {
              error = true
            }
            if (v2.id !== 100 || v2.name !== 'name2') {
              error = true
            }
          }
          
          Djn.Test.check( "test_paramsAsHashSetToTrue", !error, "Found errors" );
        }
      },                    
      baseParams: { base : true}
    });
    myStore.load({
      params: { param1: 1, param2: 'myValue', param3: null, param4 : {a:1, b:false} }
    });
  }
*/  
}


Djn.FormSupportTest = {
  testClassName: 'FormSupportTest',

  beforeClass : function() {
    var form = new Ext.FormPanel({      
      url: Djn.test.PROVIDER_BASE_URL,
      frame: true,
      width: 450,
      defaults: {
        width: 230
      },
      defaultType: 'textfield',
      items: [ input1 = new Ext.form.TextField(
        {
          fieldLabel: 'Value 1',
          name: 'input1'
        }),
        input2 = new Ext.form.TextField({
          fieldLabel: 'Value 2',
          name: 'input2'
        })
      ],
      buttons: [successfulPostButton = 
        {
          text: 'Post with successful response',
          handler: function() {
            FormSupportTest.handleForm(form.getForm().el, function(result, e) {
            });
          }
        }, 
        serverExceptionPostButton  = {
          text: 'Post that forces server error',
          handler: function() {
            FormSupportTest.handleFormCausingServerException(form.getForm().el, function(result, e) {
              if (e.type === "exception") {
                Ext.MessageBox.alert("Form post finished with server error", "Error: " + e.message);
              }
              else {
                Ext.MessageBox.alert("BUG! Test failed: ", "We expected this to be successful");
              }
            });
          }
        }
      ]
    });
  
    form.hide();
    form.render("simpleForm");
  
    // Initialize simple form for testing
    Djn.FormSupportTest.simpleForm = form;
    Djn.FormSupportTest.successfulPostButton = successfulPostButton;
    Djn.FormSupportTest.serverExceptionPostButton = serverExceptionPostButton;
    Djn.FormSupportTest.input1 = input1;
    Djn.FormSupportTest.input2 = input2;        
  },
  
  afterClass : function() {
    Djn.FormSupportTest.simpleForm.destroy();
    Djn.FormSupportTest.simpleForm = null;
    Djn.FormSupportTest.successfulPostButton = null;
    Djn.FormSupportTest.serverExceptionPostButton = null;
    Djn.FormSupportTest.input1 = null;        
    Djn.FormSupportTest.input2 = null;        
  },
  
  beforeMethod : function() {
    Djn.FormSupportTest.input1.setValue( "" );
    Djn.FormSupportTest.input2.setValue( "" );
  },
  
  test_formPostForNonAnnotatedMethod : function() {
    Djn.FormSupportTest.successfulPostButton.handler = function() {
      FormSupportTest.test_formPostForNonAnnotatedMethod(Djn.FormSupportTest.simpleForm.getForm().el, function(result, response){
        Djn.Test.checkSuccessfulResponse("test_formPostForNonAnnotatedMethod", response, response.result == true);
      });
    }

    // Simulate click!
    Djn.FormSupportTest.successfulPostButton.handler.call(
      Djn.FormSupportTest.successfulPostButton.scope, 
      Djn.FormSupportTest.successfulPostButton, Ext.EventObject);
  },

  
  test_handleForm : function() {
    // Test potentially "evil" characters, "=" and "&"
    Djn.FormSupportTest.input1.setValue( "value=&1" );
    Djn.FormSupportTest.successfulPostButton.handler = function() {
      FormSupportTest.test_handleForm(Djn.FormSupportTest.simpleForm.getForm().el, function(result, response){
        Djn.Test.checkSuccessfulResponse("test_handleForm", response, response.result === "value=&1");
      });
    }

    // Simulate click!
    Djn.FormSupportTest.successfulPostButton.handler.call(
      Djn.FormSupportTest.successfulPostButton.scope, 
      Djn.FormSupportTest.successfulPostButton, Ext.EventObject);
  },
  
  test_handleFormCausingServerException : function() {
    Djn.FormSupportTest.input1.setValue( "programmatic value" );
    Djn.FormSupportTest.serverExceptionPostButton.handler = function() {
      FormSupportTest.test_handleFormCausingServerException(Djn.FormSupportTest.simpleForm.getForm().el, function(result, response){
        Djn.Test.checkServerErrorResponse("test_handleFormCausingServerException", response, 'MyServerException');
      });
    }

    // Simulate click!
    Djn.FormSupportTest.serverExceptionPostButton.handler.call(
      Djn.FormSupportTest.serverExceptionPostButton.scope, 
      Djn.FormSupportTest.serverExceptionPostButton, Ext.EventObject);
  }

/*
  , test_handleFormWithMultivaluedItem : function() {
    var group;
    var postButton;
    var form = new Ext.FormPanel({      
      url: Djn.test.PROVIDER_BASE_URL,
      frame: true,
      width: 450,
      defaults: {
        width: 230
      },
      items: [
        {
          id : 'projects',
          xtype : 'checkboxgroup',
          items : [
            {boxLabel: 'A', name: 'projects1', inputValue: 'A'},
            {boxLabel: 'B', name: 'projects2', inputValue: 'B'},
            {boxLabel: 'C', name: 'projects3', inputValue: 'C'},
            {boxLabel: 'D', name: 'projects4', inputValue: 'D'},
            {boxLabel: 'E', name: 'projects5', inputValue: 'E'}
          ]
        }
      ],
      buttons: [postButton = 
        {
          text: 'Post'
        }
      ]
    });
    
    form.hide();
    form.render("simpleForm");
    group = form.items.items[0];

    group.setValue([true,false, true,false,true]);
    postButton.handler = function() {
      FormSupportTest.test_handleFormWithMultivaluedItem(form.getForm().el, function(result, response){
        Djn.Test.checkSuccessfulResponse("test_handleFormWithMultivaluedItem", response, response.result === 3);
      });
    }

    // Simulate click!
    postButton.handler.call(
      postButton.scope, 
      postButton, Ext.EventObject);
  } 
*/
}

Djn.FormUploadSupportTest = {
  testClassName: 'FormUploadSupportTest',

  beforeClass : function() {
    var fileUpload1;
    var fileUpload2;
    var input1;
    var input2;
    var form = new Ext.FormPanel({
      url: Djn.test.PROVIDER_BASE_URL,
      frame: true,
      width: 450,
      fileUpload : true,
      defaults: {
        width: 230
      },
      defaultType: 'textfield',
      items: [ 
        input1 = new Ext.form.TextField({
          fieldLabel: 'Value 1',
          name: 'input1'
        }),
        input2 = new Ext.form.TextField({
          fieldLabel: 'Value 2',
          name: 'input2'
        }),
        fileUpload1 = new Ext.form.FileUploadField({
          buttonOnly: true,
          id: 'form-file',
          fieldLabel: 'File 1',
          name: 'fileUpload1',
          buttonCfg: {
            test: '...'
          }
        }),
        fileUpload2 = new Ext.form.FileUploadField({
          buttonOnly: true,
          id: 'form-file-2',
          fieldLabel: 'File 2',
          name: 'fileUpload2',
          buttonCfg: {
            test: '...'
          }
        })
      ],
      buttons: [
        successfulPostButton = {
          text: 'Post with successful response',
          handler: function(){
            FormSupportTest.handleForm(form.getForm().el, function(result, e) {
            });
          }
        }, 
        serverExceptionPostButton  = {
          text: 'Post that forces server error',
          handler: function() {
            FormSupportTest.handleFormCausingServerException(form.getForm().el, function(result, e) {
              if (e.type === "exception") {
                Ext.MessageBox.alert("Form post finished with server error", "Error: " + e.message);
              }
              else {
                Ext.MessageBox.alert("BUG! Test failed: ", "We expected this to be successful");
              }
            });
          }
        }
      ]
    });
  
    form.hide();
    form.render("simpleForm");
  
    // Initialize simple form for testing
    Djn.FormUploadSupportTest.form = form;
    Djn.FormUploadSupportTest.successfulPostButton = successfulPostButton;
    Djn.FormUploadSupportTest.serverExceptionPostButton = serverExceptionPostButton;
    Djn.FormUploadSupportTest.input1 = input1;
    Djn.FormUploadSupportTest.input2 = input2;
  },
  
  afterClass : function() {
    Djn.FormUploadSupportTest.form.destroy();
    Djn.FormUploadSupportTest.form = null;
    Djn.FormUploadSupportTest.successfulPostButton = null;
    Djn.FormUploadSupportTest.serverExceptionPostButton = null;
    Djn.FormUploadSupportTest.input1 = null;        
    Djn.FormUploadSupportTest.input2 = input2;
  },
  
  beforeMethod : function() {
    Djn.FormUploadSupportTest.input1.setValue("");
    Djn.FormUploadSupportTest.input2.setValue("");
  },
  
  test_handleUploadFormWithNoFiles: function(){
    Djn.FormUploadSupportTest.input1.setValue("value1");
    
    Djn.FormUploadSupportTest.successfulPostButton.handler = function(){
      FormUploadSupportTest.test_handleUploadFormWithNoFiles(Djn.FormUploadSupportTest.form.getForm().el, function(result, response){
        Djn.Test.checkSuccessfulResponse("test_handleUploadFormWithNoFiles", response, response.result === true);
      });
    }

    // Simulate click!
    Djn.FormUploadSupportTest.successfulPostButton.handler.call(
      Djn.FormUploadSupportTest.successfulPostButton.scope, 
      Djn.FormUploadSupportTest.successfulPostButton, Ext.EventObject);
  },

  test_doubleQuoteReturnedCorrectlyInUploadForm: function(){
    Djn.FormUploadSupportTest.input1.setValue("a\"b\"");
    
    Djn.FormUploadSupportTest.successfulPostButton.handler = function(){
      FormUploadSupportTest.test_doubleQuoteReturnedCorrectlyInUploadForm(Djn.FormUploadSupportTest.form.getForm().el, function(result, response){
        Djn.Test.checkSuccessfulResponse("test_doubleQuoteReturnedCorrectlyInUploadForm", response, response.result === "a\"b\"");
      });
    }

    // Simulate click!
    Djn.FormUploadSupportTest.successfulPostButton.handler.call(
      Djn.FormUploadSupportTest.successfulPostButton.scope, 
      Djn.FormUploadSupportTest.successfulPostButton, Ext.EventObject);
  },

  test_handleUploadFormCausingServerException : function() {
    Djn.FormUploadSupportTest.serverExceptionPostButton.handler = function() {
      FormUploadSupportTest.test_handleUploadFormCausingServerException(Djn.FormUploadSupportTest.form.getForm().el, function(result, response){
        Djn.Test.checkServerErrorResponse("test_handleUploadFormCausingServerException", response, 'MyServerException');
      });
    }

    // Simulate click!
    Djn.FormUploadSupportTest.serverExceptionPostButton.handler.call(
      Djn.FormUploadSupportTest.serverExceptionPostButton.scope, 
      Djn.FormUploadSupportTest.serverExceptionPostButton, Ext.EventObject);
  }

}


Djn.PollTest = {
  testClassName: 'PollTest',
  
  test_pollForNonAnnotatedMethod : function() {
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 100,
      url: Djn.test.POLLING_URLS.test_pollForNonAnnotatedMethod,
      listeners: {
        data: function(provider, event) {
          Ext.log( 'test_pollWithNoBaseParams');
          timesCalled++;
          if (timesCalled === 2) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollForNonAnnotatedMethod', event.data !== undefined && event.data.indexOf('Ok') === 0, "Expected to receive 'Ok' as event.data");
          }
        }
      }
    });
    pollingProvider.connect();
  },
  
  test_pollWithNoBaseParams: function(){
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 100,
      url: Djn.test.POLLING_URLS.test_pollWithNoBaseParams,
      listeners: {
        data: function(provider, event) {
          Ext.log( 'test_pollWithNoBaseParams');
          timesCalled++;
          if (timesCalled === 2) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollWithNoBaseParams', event.data !== undefined && event.data.indexOf('Ok') === 0, "Expected to receive 'Ok' as event.data");
          }
        }
      }
    });
    pollingProvider.connect();
  },

  test_pollCausingServerError: function(){
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 100,
      url: Djn.test.POLLING_URLS.test_pollCausingServerError,
      listeners: {
        data: function(provider, event) {
          Ext.log( 'test_pollCausingServerError (' + timesCalled + ')');
          timesCalled++;
          if (timesCalled === 2) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollCausingServerError', event.data === undefined && event.message.indexOf('MyServerException') === 0, "Expected to receive a MyServerException, but error message was " + event.message);
          }
        }
      }
    });
    pollingProvider.connect();
  },

  test_pollWithBaseParams: function(){
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 100,
      url: Djn.test.POLLING_URLS.test_pollWithBaseParams,
      baseParams : {
        arg1 : 'value'
      },
      listeners: {
        data: function(provider, event) {
          Ext.log( 'test_pollWithBaseParams');
          timesCalled++;
          if (timesCalled === 2) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollWithBaseParams', event.data !== undefined && event.data === 'arg1=value', "Expected to receive 'arg1=value' as event.data");
          }
        }
      }
    });
    pollingProvider.connect();
  }
}

Djn.CustomGsonBuilderHandlingTest = {
  testClassName : "CustomGsonBuilderHandlingTest", 
  
  test_specialDateDeserialization : function() {
    var aDate = {year: 2005, month: 3, day: 20};
    CustomGsonBuilderHandlingTest.test_specialDateDeserialization(aDate, function(result, response){
      Djn.Test.checkSuccessfulResponse("test_specialDateDeserialization", response, response.result === true);
    });
  },

  test_specialDateSerialization : function() {
    var aDate = {year: 2005, month: 3, day: 20};
    CustomGsonBuilderHandlingTest.test_specialDateSerialization(function(result, response){
      Djn.Test.checkSuccessfulResponse("test_specialDateSerialization", response, result.year === 2007 && result.month === 5 && result.day === 29);
    });
  }
}

Djn.NamespaceTest = {
  testClassName: 'NamespaceTest',
  
  test_namespacedAction : function() {
    Djn.actionsNamespace.NamespaceTest.test_namespacedAction(function(result, response){
      Djn.Test.checkSuccessfulResponse("test_namespacedAction", response, result === true);
    });
  }
}

Djn.CustomRegistryConfiguratorHandlingTest = {
  testClassName : 'CustomRegistryConfiguratorHandlingTest',
  
  test_programmaticMethod :function() {
    Djn.programmaticNamespace.MyCustomRegistryConfiguratorHandlingTest.myProgrammaticMethod( 'programmatic', function(result, response) {
      Djn.Test.checkSuccessfulResponse("test_programmaticMethod", response, result === 'programmatic');
    });
  },

  test_programmaticPollMethod : function() {
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 300,
      baseParams : {
        myParameter : 'myValue'
      },
      url: Djn.programmaticNamespace.POLLING_URLS.myProgrammaticPollMethod,
      listeners: {
        data: function(provider, event) {
          Ext.log( 'test_programmaticPollMethod');
          pollingProvider.disconnect();
          timesCalled++;
          if( timesCalled == 1 ) {
            Djn.Test.check('test_programmaticPollMethod', event.data === 'ok', "Expected to receive 'ok' as event.data");
          }
        }
      }
    });
    pollingProvider.connect();
  }
  
}

Djn.MethodsInBaseClassCorrectlyScannedTest = {
  testClassName : 'MethodsInBaseClassCorrectlyScannedTest',
  
  test_methodsInBaseClassCorrectlyScanned :function() {
    MethodsInBaseClassCorrectlyScannedTest.test_serverReturningNothing( function(result, response) {
        Djn.Test.checkSuccessfulResponse( "test_methodsInBaseClassCorrectlyScanned", response, response.result === null, response.result);
    });
  }
},

Djn.ApplicationStatefulActionTest = {
  testClassName : 'ApplicationStatefulActionTest',
  
  test_getApplicationData : function() {
    ApplicationStatefulActionTest.test_getApplicationData( function(result, response) {
      ApplicationStatefulActionTest.test_getApplicationData( function(result, response) {
        Djn.Test.checkSuccessfulResponse( "test_getApplicationData", response, response.result > 1, response.result);
      })
    });
  },
  test_pollForApplicationScopedAction: function(){
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 300,
      url: Djn.test.POLLING_URLS.test_pollForApplicationScopedAction,
      listeners: {
        data: function(provider, event) {
          timesCalled++;
          var data = event.data;
          if (timesCalled === 20) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollForApplicationScopedAction', data > 1, data);
          }
        }
      }
    });
    pollingProvider.connect();
  }
  
},

Djn.SessionStatefulActionTest = {
  testClassName : 'SessionStatefulActionTest',
  
  test_getSessionData : function() {
    SessionStatefulActionTest.test_getSessionData( function(result, response) {
      SessionStatefulActionTest.test_getSessionData( function(result, response) {
        Djn.Test.checkSuccessfulResponse( "test_getSessionData", response, response.result > 1, response.result);
      })
    });
  },
  test_pollForSessionScopedAction: function(){
    var timesCalled = 0;
    var pollingProvider = Ext.Direct.addProvider({
      type: 'polling',
      interval: 100,
      url: Djn.test.POLLING_URLS.test_pollForSessionScopedAction,
      listeners: {
        data: function(provider, event) {
          timesCalled++;
          var data = event.data;
          if (timesCalled === 20) {
            pollingProvider.disconnect();
            Djn.Test.check('test_pollForSessionScopedAction', data > 1, data);
          }
        }
      }
    });
    pollingProvider.connect();
  }
  
},


Djn.ClassWithMultipleActionsTest = {
  testClassName : 'ClassWithMultipleActionsTest',
  
  test_callDifferentActionsForSameJavaClass : function() {
    action1.getValue( function(result, response) {
      var value = result;
      action2.getValue( function(result, response) {
        Djn.Test.checkSuccessfulResponse( "test_callDifferentActionsForSameJavaClass", response, result == 25 && value == 25, response.result);
      });
    });
  }
}

