import requests
import json

url = "https://mpiptfzjrixbzpzpxjfj.supabase.co/auth/v1/otp"
headers = {
    "apikey": "sb_publishable_pab_R5GFZYBOYtpfN_JF1Q_Xf1UrHWP",
    "Authorization": "Bearer sb_publishable_pab_R5GFZYBOYtpfN_JF1Q_Xf1UrHWP",
    "Content-Type": "application/json"
}

def test_otp():
    payload = {
        "email": "pd3690das@gmail.com",
        "create_user": True
    }
    response = requests.post(url, headers=headers, json=payload)
    print(f"OTP Flow: {response.status_code} - {response.text}")

def test_signup():
    signup_url = "https://mpiptfzjrixbzpzpxjfj.supabase.co/auth/v1/signup"
    payload = {
        "email": "pd3690das@gmail.com",
        "password": "TemporaryPassword123!"
    }
    response = requests.post(signup_url, headers=headers, json=payload)
    print(f"Signup Flow: {response.status_code} - {response.text}")


print("Testing OTP Flow (defaults to Magic Link without correct template)")
test_otp()

print("\nTesting Signup Flow (should send a 6-digit code via 'Confirm Signup' template)")
test_signup()
