# WalletStats
App for controlling and managing personal money. Written in Java for Android. <br>
Inspired by the financial app Organizze. The app can be found at https://play.google.com/store/apps/details?id=com.organizze.android. <br> or at https://www.organizze.com.br.

<h2>DEPENDENCIES</h2>

Floating Action Menu - https://github.com/Clans/FloatingActionButton

Material Calendar View - https://github.com/prolificinteractive/material-calendarview

MPAndroidChart - https://github.com/PhilJay/MPAndroidChart

Firebase Firestore & auth - https://firebase.google.com/docs/android/setup

<h2>SCREENS</h2> 

<h4>Login Screen</h4>
<p>
  Here the user can access it's account or go to the registration screen to create a new one.
</p>
<p>
  <strong>*Uses</strong> Firebase Authentication  
</p>
<img src="images/ws_login.png" width="300" height="500">

<h4>Register Screen</h4>
<p>
  Here the user can create a new account.
</p>
<p>
  <strong>*Uses</strong> Firebase Authentication & Firebase Firestore
</p>
<img src="images/ws_register.png" width="300" height="500">

<h4>Main Screen</h4>
<p>
  The main screen is where the user's transactions and account balance are displayed. <br>
  The list is filtered by the month and year. the user can switch the date by swiping the calendar in the middle of the screen <br>
  or by clicking the arrows on the edges. <br>
  By clicking in the fab menu the user has the option to add a new expense or revenue to his account.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore, Floating Action Menu & Material Calendar View 
</p>
<img src="images/ws_main.png" width="300" height="500">

<h4>Add Revenue Screen</h4>
<p>
  Here the user can add a new revenue to his account in the specified date. <br>
  By default the date input is set to the current date, but can be easily changed.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore
</p>
<img src="images/ws_revenue.png" width="300" height="500">

<h4>Add Expense Screen</h4>
<p>
  Here the user can add a new expense to his account in the specified date. <br>
  By default the date input is set to the current date, but can be easily changed.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore
</p>
<img src="images/ws_expense.png" width="300" height="500">

<h4>Populated Main Screen</h4>
<p>
  After populating the account with some transactions, they are listed in the the main screen. <br>
  The list is ordered by the type of transaction where the revenues are listed first followed by the expenses. <br>
  Below the username is located the account balance, summing all the revenues registered and subtracting the sum of all the expenses    registered as well.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore & Material Calendar View
</p>
<img src="images/ws_main_updated.png" width="300" height="500">

<h4>Deleting Transactions</h4>
<p>
  In case the user has to delete a transaction, it can be easily done be simple swiping one of the items in the list. <br>
  After swiping, a alert dialog will be displayed with two options, one to proceed with the action and another one to canceling it.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore & Material Calendar View
</p>
<img src="images/ws_delete.png" width="300" height="500">

<h4>Graph Screen</h4>
<p>
  The graph screen can be shown by clicking the "graph" icon in the action bar on the main screen. <br>
  There is displayed the sum of the revenues in a given month as well as the sum of the expenses. <br>
  In case the user wants a visual representation of its wallet stats.
</p>
<p>
  <strong>*Uses</strong> Firebase Firestore, Material Calendar View & MPAndroidChart
</p>
<img src="images/ws_graph.png" width="300" height="500">
