package com.mobile.maahita

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.util.Base64
import androidx.annotation.Keep
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mobile.maahita.models.JaasSettings
import com.mobile.maahita.utilities.Utility
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import java.io.BufferedReader
import java.io.StringReader
import java.net.URL
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import kotlin.collections.HashMap

class MeetingManager(private var context: Context) {

    fun getMeetingOptions(meetingID: String, title: String): JitsiMeetConferenceOptions? {
        try {
            Utility.getJsonDataFromAsset(context, "Jaas-service.json")?.let { jsonFileString ->
                print(jsonFileString)
                val gson = Gson()
                val listPersonType = object : TypeToken<JaasSettings>() {}.type

                var settings: JaasSettings = gson.fromJson(jsonFileString, listPersonType)

                val pkcs8Lines = StringBuilder()
                val rdr = BufferedReader(StringReader(settings.privatekey))
                var line: String?
                while (rdr.readLine().also { line = it } != null) {
                    pkcs8Lines.append(line)
                }

                var pkcs8Pem = pkcs8Lines.toString()
                pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "")
                pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "")
                pkcs8Pem = pkcs8Pem.replace("\\s+".toRegex(), "")

                // Base64 decode the result
                val pkcs8EncodedBytes: ByteArray = Base64.decode(pkcs8Pem, Base64.DEFAULT)

                val keySpec = PKCS8EncodedKeySpec(pkcs8EncodedBytes)
                val kf = KeyFactory.getInstance("RSA")
                val privateKey: PrivateKey = kf.generatePrivate(keySpec)

                val header: HashMap<String, Any?> = HashMap<String, Any?>();
                val claims: HashMap<String, Any?> = HashMap<String, Any?>();

                //Setting header
                header.put(JwsHeader.KEY_ID, settings.kid)
                header.put(JwsHeader.ALGORITHM, SignatureAlgorithm.RS256)
                header.put(JwsHeader.TYPE, JwsHeader.JWT_TYPE)

                var displayName = "māhita user"
                var avatar = ""
                var userid = ""
                var emailid = ""

                FirebaseAuth.getInstance().currentUser?.let { user ->
                    displayName = user.displayName ?: "māhita user"
                    user.photoUrl?.let { uri ->
                        avatar = uri.toString()
                    }
                    userid = user.uid
                    emailid = user.email ?: ""
                }

                //Setting claims
                claims.put(
                    "context", hashMapOf<String, Any>(
                        "user" to hashMapOf<String, Any>(
                            "avatar" to avatar,
                            "name" to displayName,
                            "email" to emailid,
                            "id" to userid,
                            "moderator" to "false"
                        ),
                        "features" to hashMapOf<String, Any>(
                            "livestreaming" to "false",
                            "recording" to "false",
                            "outbound-call" to "false",
                            "transcription" to "false"
                        )
                    )
                )
                claims.put("room", meetingID)
                claims.put("aud", "jitsi")

                val now = Date()

                val tokenString: String = Jwts.builder()
                    .setHeader(header)
                    .setClaims(claims)
                    .setIssuer("chat")
                    .setSubject(settings.appid)
                    .setExpiration(Date(now.time + 2 * 1000 * 60 * 60))
                    .setIssuedAt(now)
                    .signWith(privateKey)
                    .compact()

                val serverURL = URL(settings.server)

                val options = JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverURL)
                    .setToken(tokenString)
                    .setRoom(settings.server + "/" + settings.appid + "/" + meetingID)
                    .setSubject(title)
                    .setAudioMuted(true)
                    .setVideoMuted(true)
                    .setAudioOnly(false)
                    .setFeatureFlag("live-streaming.enabled", false)
                    .setFeatureFlag("recording.enabled", false)
                    .setFeatureFlag("invite.enabled", false)
                    .setFeatureFlag("pip.enabled", false)
                    .setFeatureFlag("filmstrip.enabled", false)
                    .setWelcomePageEnabled(false)
                    .build()

                return options
            }
            return null
        } catch (exception: Exception) {
            print(exception)
            return null
        }
    }
}