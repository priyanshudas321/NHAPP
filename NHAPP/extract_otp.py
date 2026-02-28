import os
import zipfile

jar_path = r"C:\Users\priyanshu das\.gradle\caches\modules-2\files-2.1\io.github.jan-tennert.supabase\gotrue-kt-android-debug\2.1.0\cb962cbfc71a56454689c6608fe03db91721c915\gotrue-kt-android-debug-2.1.0-sources.jar"
try:
    with zipfile.ZipFile(jar_path, 'r') as z:
        for name in z.namelist():
            if "OTP" in name or "Otp" in name:
                print(f"Found: {name}")
                if "OTP.kt" in name or "OtpConfig.kt" in name or "OTPProvider.kt" in name:
                    with open(name.replace("/", "_"), "w", encoding="utf-8") as f:
                        f.write(z.read(name).decode('utf-8'))
                    print(f"Extracted {name}")
except Exception as e:
    print(e)
