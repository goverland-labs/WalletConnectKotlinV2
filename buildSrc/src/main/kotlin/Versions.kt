import org.gradle.api.JavaVersion

const val KEY_PUBLISH_VERSION = "PUBLISH_VERSION"
const val KEY_PUBLISH_ARTIFACT_ID = "PUBLISH_ARTIFACT_ID"
const val KEY_SDK_NAME = "SDK_NAME"

//Latest versions
const val BOM_VERSION = "1.31.2"
const val FOUNDATION_VERSION = "1.17.0"
const val CORE_VERSION = "1.31.2"
const val SIGN_VERSION = "2.31.2"
const val AUTH_VERSION = "1.28.1"
const val CHAT_VERSION = "1.0.0-beta29"
const val NOTIFY_VERSION = "1.3.1"
const val WEB_3_WALLET_VERSION = "1.31.2"
const val WEB_3_MODAL_VERSION = "1.5.2"
const val WC_MODAL_VERSION = "1.5.2"
const val MODAL_CORE_VERSION = "1.5.2"

val jvmVersion = JavaVersion.VERSION_11
const val MIN_SDK: Int = 23
const val TARGET_SDK: Int = 33
const val COMPILE_SDK: Int = TARGET_SDK
val SAMPLE_VERSION_CODE = BOM_VERSION.replace(".", "").toInt()
const val SAMPLE_VERSION_NAME = BOM_VERSION
const val agpVersion = "8.0.2" // when changing, remember to change version in build.gradle.kts in buildSrc module
const val kotlinVersion = "1.8.21" // when changing, remember to change version in build.gradle.kts in buildSrc module
const val kspVersion = "$kotlinVersion-1.0.11"
const val dokkaVersion = "1.7.10" // when changing, remember to change version in build.gradle.kts in buildSrc module
const val googleServiceVersion = "4.3.15"

const val sqlDelightVersion = "1.5.4"
const val moshiVersion = "1.15.0"
const val coroutinesVersion = "1.7.1"
const val composeCompilerVersion = "1.4.7"
const val composeBomVersion = "2023.05.01"
const val composeNavigationVersion = "2.5.3"
const val composeViewModelVersion = "2.6.1"
const val scarletVersion = "1.0.1"
const val scarletPackage = "com.walletconnect.Scarlet"
const val koinVersion = "3.4.2"
const val lifecycleVersion = "2.6.1"
const val navVersion = "2.5.3"
const val retrofitVersion = "2.9.0"
const val okhttpVersion = "4.11.0"
const val bouncyCastleVersion = "1.77"
const val sqlCipherVersion = "4.5.3@aar"
const val multibaseVersion = "1.1.1"
const val jUnit4Version = "4.13.2"
const val androidxTestVersion = "1.5.0"
const val androidxTestOrchestratorVersion = "1.4.2"
const val robolectricVersion = "4.11.1"
const val mockkVersion = "1.13.5"
const val jsonVersion = "20220924"
const val timberVersion = "5.0.1"
const val androidSecurityVersion = "1.1.0-alpha06"
const val web3jVersion = "4.9.8-wc"
const val kethereumVersion = "0.85.7"
const val wsRestJavaVersion = "3.1.0"
const val relinkerVersion = "1.4.5"
const val accompanistVersion = "0.31.3-beta"
const val coreKtxVersion = "1.10.1"
const val appCompatVersion = "1.6.1"
const val materialVersion = "1.9.0"
const val coilVersion = "2.3.0"
const val customQrGeneratorVersion = "1.6.2"
const val turbineVersion = "1.0.0"
const val firebaseBomVersion = "32.2.0"
const val paparazziVersion = "1.3.1"
const val beagleVersion = "2.9.0"
const val dataStoreVersion = "1.0.0"
const val coinbaseVersion = "1.0.4"
