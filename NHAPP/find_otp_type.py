import os
import zipfile

cache_dir = os.path.expanduser(r"~\.gradle\caches\modules-2\files-2.1\io.github.jan-tennert.supabase\gotrue-kt\2.1.0")
for root, dirs, files in os.walk(cache_dir):
    for file in files:
        if file.endswith("-sources.jar"):
            jar_path = os.path.join(root, file)
            print(f"Checking {jar_path}")
            try:
                with zipfile.ZipFile(jar_path, 'r') as z:
                    for name in z.namelist():
                        if "OtpType.kt" in name:
                            print(f"Found {name}!\n---")
                            print(z.read(name).decode('utf-8'))
                            exit(0)
            except Exception as e:
                pass
