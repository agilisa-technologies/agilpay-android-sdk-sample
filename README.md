# agilpay-android-sdk-sample
Sample project that explains how to use and consume the Paynet SDK

**Live demo:**
https://www.figma.com/proto/k0M65ur5kWDoiZAHD8xORN/Paynet-SDK-Mobile-Xamarin.Forms?type=design&node-id=41-1892&t=NfwjDxvHIkTidwyt-1&scaling=scale-down&page-id=0%3A1&starting-point-node-id=41%3A1892&mode=design

# Setup
* Step 1. Add the token to $HOME/.gradle/gradle.properties:
  ``` java
  authToken=jp_8cd5tquai09929pdcscm6cs76j
  ```
* Step 2. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
``` java
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven {
        url 'https://jitpack.io'
        credentials { username authToken }
      }
		}
	}
```
* Step 3. Add the dependency
``` java
dependencies {
  implementation 'com.github.agilisa-technologies:paynet-mobile-sdk-android:Tag'
}
```


# Getting Started
Once the gradle dependencies are configured, you must initialize the sdk by calling the Initialice method:
 

```java
String apiUrl = "https://sandbox-webapi.agilpay.net/";
String clientId = "API-001";
String clientSecret = "Dynapay";
PaymentServices.initialize(apiUrl, clientId, clientSecret, new InitializationCallback() {
                @Override
                public void onInitSuccess() {
                    //now you can use the sdk
                }

                @Override
                public void onInitFailed(String error) {
                    //an error has occurred
                }
            });
```
Once the initialization process has been completed you will get a response in the OnInitSuccess method.

**The environment URLs are:**
* for test environment: https://sandbox-webpay.agilpay.net/ 
* for production environment: https://webpay.agilpay.net/ 

Now you are ready to use process payments

# Usage

```java
PaymentRequest request = new PaymentRequest();
        request.setCustomerId(customerId);
        request.setAmount(amount);
        request.setCurrency(currency);
        request.setCustomerEmail(email);
        request.setCustomerName(customerName);
        request.setInvoice(invoice);
        request.setMerchantKey(merchantKey);
        request.setTax(tax);
        request.setForceDuplicate(true); //optional
        request.setHoldFunds(false); //optional

        PaymentServices.processPayment(context, request, new PaymentCallback() {
            @Override
            public void onPaymentResult(Transaction transaction) {

                Log.d("TAG", transaction.toString());
            }

            @Override
            public void onError(String error) {
                //an error has occurred
            }
        });
```
To get the response of the process payment, we use the interface agilpay.mobile.sdk.android.abstraction.PaymentCallback, with the methods
* onPaymentResult(Transaction transaction);
* onError(string error);

if transaction.ResponseCode is equal to "00" the transaction was approved otherwise it was declined
