# HttpRouter [![Build Status](https://travis-ci.org/soxfmr/HttpRouter.svg)](https://travis-ci.org/soxfmr/HttpRouter)
HttpRouter is a HTTP library for Android implements by URLConnection.

# Features
* HTTP request chains support
* Interrupt the next request for modification
* Ban the cookies and restore it for next request

# At A Glance
* [Getting Start](#getting-start)
* [Forward A Simple HTTP Request](#0x0-forward-a-simple-http-request)
* [Forward A HTTP Post Request](#0x1-forward-a-http-post-request)
* [Attach the HTTP headers](#0x2-attach-the-http-headers)
* [HTTP Request Chains, With Powerful Middleware](#0x3-http-request-chains-with-powerful-middleware)
* [Backward to Give the HTTP Response](#0x4-backward-to-give-the-http-response)
* [To Maintain the Cookies](#0x5-to-maintain-the-cookies)

## Getting Start
Reference as a Gradle project
```groovy
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}

dependencies {
     compile 'com.github.soxfmr:HttpRouter:v0.14'
}
```

## 0x0 Forward A Simple HTTP Request
```java
HttpRouter router = new HttpRouter();
router.forward(new HttpGet("https://www.google.com"));
```

## 0x1 Forward A HTTP Post Request
```java
RequestParam params = new RequestParam();
params.addParam("q", "hello world");

HttpPost postRequest = new HttpPost("https://www.google.com");
postRequest.attachParams(params);
router.forward(postRequest);
```

You can also, however, to handle it easier
```java
router.forward(new HttpPost("https://www.google.com").addParam("p", "hello world"));
```

## 0x2 Attach the HTTP headers
```java
router.forward(new HttpPost("https://www.google.com").addHeader("User-Agent", "Android"));
```
The chains call to attach more than one header
```java
router.forward(new HttpPost("https://www.google.com")
            .addHeader("User-Agent", "Android")
            .addHeader("Accept-Charset", "UTF-8"));
```
But I am so lazy enough, em. Let's attach the basic http headers. Notice that the basic headers will be attach to the request by default when the headers is empty.
```java
router.forward(new HttpPost("https://www.google.com").attachHeaders(new BasicHttpHeader()));
```

## 0x3 HTTP Request Chains, With Powerful Middleware
You can add the request to the route queue, after forward it, the next request will be forward until the previous request was completed.
```java
router.addRoute(new HttpGet("https://www.google.com"));
router.addRoute(new HttpPost("https://www.google.com").addParam("p", "hello world"));
router.addRoute(new HttpGet("https://www.example.com"));
router.forward();
```
Sometime, you may want to modify the next request properties with the information fetch from the request previously. To achieve it, you can register a listener which is called **MiddlewareListener** and handle the next request.
```java
private static final int RC_ATTACH_INFO = 0x1;

router.registerMiddleListener(new MiddlewareListener() {

    public void handle(HttpRequest nextRequest, int requestCode, HttpResponse prevResponse) {
        if (requestCode == RC_ATTACH_INFO) {
            int firstArticleId = findFirstArticleId(prevResponse.toString());

            String origin = nextRequest.getURI();
            nextRequest.setURI(String.format(origin, firstArticleId));
            // OR
            // nextRequest.addParam("id", firstArticleId);
        }
    }

});

router.addRoute(new HttpGet("http://www.example.com"));
// Specify the request code
router.addRoute(new HttpGet("http://www.example.com/article?id=%s"), RC_ATTACH_INFO);
// OR
// router.addRoute(new HttpGet("http://www.example.com/article"), RC_ATTACH_INFO);
router.forward();
```

## 0x4 Backward to Give the HTTP Response
```java
router.setResponseListener(new ResponseListener() {
    public void onCorrupt() {
        System.out.println("Failed to forward this request or the response was corrupted.");
    }

    // RESPONSE_CODE >= 200
    public void onSuccess(HttpResponse response) {
        System.out.println("To reach every corner of the world :)");
    }

    // RESPONSE_CODE >= 300
    public void onRedirect(HttpResponse response) {
        HttpHeader responseHeaders = response.getResponseHeaders();
        System.out.println("Redirect to: " + responseHeaders.getHeaderValue("Location"));
    }

    // RESPONSE_CODE >= 400
    public void onFailed(HttpResponse response) {
        System.out.println("Missed something?");
    }

    // RESPONSE_CODE >= 500
    public void onInternalError(HttpResponse response) {
        System.out.println("An error occurred on remote internal server.");
    }
});
```
If you just want to handle it more easier, we do so
```java
router.setResponseLiteListener(new ResponseLiteListener() {

    public void onFinished(HttpResponse response) {
        System.out.println("Response Code: " + response.getResponseCode());
    }

});
```

## 0x5 To Maintain the Cookies
By default, the cookies which is receive from server will be maintain by CookieContainer automatically. You can refuse the cookeis as you want.
```java
router.forward(new HttpGet("https://www.google.com").disableCookies());
```
After you have banned the cookies, you should enable it if you want to receive the cookies in next request.
```java
// The first request will fetch the cookies from the server
router.addRoute(new HttpGet("https://www.google.com"));
// Remove all of cookies and refuse the cookies in this request
router.addRoute(new HttpGet("https://www.google.com").disableCookies());
// Notice that this request will never receive the cookies
router.addRoute(new HttpGet("https://www.google.com"));
// The cookies which was received from first request will be restore to this request
router.addRoute(new HttpGet("https://www.google.com").restoreCookies());
// When you call enableCookies() instead of restoreCookies() that the request will attach an empty cookies
router.addRoute(new HttpGet("https://www.google.com").enableCookies());
```

## Licenses and Notices
Publish under MIT License
```
The MIT License (MIT)

Copyright (c) 2015 soxfmr@foxmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

**Notices for files**: Apache HttpComponents Client
```
Copyright 1999-2015 The Apache Software Foundation

This product includes software developed at
The Apache Software Foundation (http://www.apache.org/).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
