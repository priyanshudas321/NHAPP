import requests
import json

url = "https://mpiptfzjrixbzpzpxjfj.supabase.co/auth/v1/otp"
headers = {
    "apikey": "sb_publishable_pab_R5GFZYBOYtpfN_JF1Q_Xf1UrHWP",
    "Authorization": "Bearer sb_publishable_pab_R5GFZYBOYtpfN_JF1Q_Xf1UrHWP",
    "Content-Type": "application/json"
}

def test_otp(create_user):
    payload = {
        "email": "pd3690das@gmail.com",
        "create_user": create_user
    }
    response = requests.post(url, headers=headers, json=payload)
    print(f"create_user={create_user}: {response.status_code} - {response.text}")

print("Testing with create_user=False")
test_otp(False)

print("\nTesting with create_user=True")
test_otp(True)
