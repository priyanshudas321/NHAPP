import os
import subprocess

cache_dir = os.path.expanduser(r"~\.gradle\caches\modules-2\files-2.1\io.github.jan-tennert.supabase\gotrue-kt\2.1.0")
for root, dirs, files in os.walk(cache_dir):
    for file in files:
        if file.endswith(".jar") and not file.endswith("-sources.jar"):
            jar_path = os.path.join(root, file)
            print(f"Running javap on {jar_path}")
            # Try to list classes from the jar
            result = subprocess.run(["jar", "tf", jar_path], capture_output=True, text=True)
            for line in result.stdout.splitlines():
                if "OtpType" in line:
                    print(line)
                    # if it's a class file, run javap on it
                    if line.endswith(".class"):
                        class_name = line.replace("/", ".").replace(".class", "")
                        print(f"--- javap {class_name} ---")
                        javap_result = subprocess.run(["javap", "-cp", jar_path, class_name], capture_output=True, text=True)
                        print(javap_result.stdout)
