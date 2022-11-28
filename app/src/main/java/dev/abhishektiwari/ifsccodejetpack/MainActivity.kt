package dev.abhishektiwari.ifsccodejetpack

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.telecom.Call.Details
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import dev.abhishektiwari.ifsccodejetpack.ui.theme.IFSCCODEJetPackTheme
import dev.abhishektiwari.ifsccodejetpack.ui.theme.Purple200
import dev.abhishektiwari.ifsccodejetpack.ui.theme.greenColor
import org.w3c.dom.Text

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IFSCCODEJetPackTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        topBar = {
                            TopAppBar(backgroundColor = Purple200,
                                title = {
                                    // in the top bar we are specifying
                                    // tile as a text
                                    Text(
                                        // on below line we are specifying
                                        // text to display in top app bar.
                                        text = "Ifsc Code Decode",

                                        color = Color.White,

                                        // on below line we are specifying
                                        // modifier to fill max width.
                                        modifier = Modifier.fillMaxWidth(),

                                        // on below line we are
                                        // specifying text alignment.
                                        textAlign = TextAlign.Center,

                                        // on below line we are
                                        // specifying color for our text.
                                    )
                                }
                            )
                        }
                    ) {
                        // on below line we are
                        // calling method to display UI.
                        getBankDetails()
                    }
                }
            }
        }
    }
}

@Composable
fun getBankDetails() {

    // in the below line, we are
    // creating variables for URL
    val ifscCode = remember {
        mutableStateOf(TextFieldValue())
    }
    val bankDetails = remember {
        mutableStateOf("")
    }


    // on below line we are creating
    // a variable for a context
    val ctx = LocalContext.current

    // on below line we are creating a column
    Column(
        // on below line we are specifying modifier
        // and setting max height and max width
        // for our column
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth()
            // on below line we are
            // adding padding for our column
            .padding(5.dp),

        // on the below line we are specifying horizontal
        // and vertical alignment for our column
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // on the below line, we are creating a text field.
        TextField(
            // on below line we are specifying
            // value for our  text field.
            value = ifscCode.value,

            // on below line we are adding on value
            // change for text field.
            onValueChange = { ifscCode.value = it },

            // on below line we are adding place holder as text
            placeholder = { Text(text = "Enter IFSC Code") },

            // on below line we are adding modifier to it
            // and adding padding to it and filling max width
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),

            // on below line we are adding text style
            // specifying color and font size to it.
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),

            // on below line we are adding single line to it.
            singleLine = true,
        )

        // on below line adding a spacer.
        Spacer(modifier = Modifier.height(10.dp))

        // on below line adding a button to send an email
        Button(onClick = {
            getBankDetailsFromIFSC(ifscCode.value.text, bankDetails, ctx)

        }) {
            // on below line creating a text for our button.
            Text(
                // on below line adding a text ,
                // padding, color and font size.
                text = "Get Bank Details",
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 15.sp
            )
        }

        // on below line adding a spacer
        Spacer(modifier = Modifier.height(25.dp))

        // on below line creating a textview for displaying bank details
        Text(bankDetails.value, fontSize = 15.sp, color = greenColor, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }

}

fun getBankDetailsFromIFSC(ifscCode: String, bankDetails: MutableState<String>, ctx: Context) {
    // on below line we are creating
    // a variable for our url.
    var url = "https://ifsc.razorpay.com/" + ifscCode

    // on below line we are creating a variable for
    // our request queue and initializing it.
    val queue: RequestQueue = Volley.newRequestQueue(ctx)

    // on below line we are creating a variable for request
    // and initializing it with json object request
    val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->

        // this method is called when we get a successful response from API.

        // on below line we are adding a try catch block.
        try {
            // on the below line we are getting
            // data from our response
            // and setting it in variables.
            val bankName: String = response.getString("BANK")
            val micr: String = response.getString("MICR")
            val contact: String = response.getString("CONTACT")
            val address: String = response.getString("ADDRESS")
            val branch: String = response.getString("BRANCH")

            // on below line we are setting
            // data to our variables which
            // we have passed.
            bankDetails.value =
                "Bank Name : " + bankName + "\n" + "MICR Code : " + micr + "\nContact Number : " + contact + "\nAddress : " + address + "\nBranch : " + branch

        } catch (e: Exception) {
            // on below line we are
            // handling our exception.
            e.printStackTrace()
        }

    }, { error ->
        // this method is called when we get any error while
        // fetching data from our API
        // in this case we are simply displaying a toast message.
        Toast.makeText(ctx, "Fail to get response", Toast.LENGTH_SHORT)
            .show()
    })
    // at last we are adding
    // our request to our queue.
    queue.add(request)
}



