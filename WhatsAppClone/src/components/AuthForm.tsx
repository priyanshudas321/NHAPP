/* eslint-disable @typescript-eslint/no-unsafe-member-access */

"use client";

import "react-toastify/dist/ReactToastify.css";

import { yupResolver } from "@hookform/resolvers/yup";
import axios from "axios";
import Lottie from "lottie-react";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { signIn, signOut, useSession } from "next-auth/react";
import { useTheme } from "next-themes";
import React from "react";
import { FieldValues, SubmitHandler, useForm } from "react-hook-form";
import { BsGithub } from "react-icons/bs";
import { FcGoogle } from "react-icons/fc";
import { BeatLoader } from "react-spinners";
import { toast, ToastContainer } from "react-toastify";
import * as Yup from "yup";

import animationData from "@/assets/animation_lkgam3yw.json";
import { UserSession } from "@/lib/model";

import AuthSocialButton from "./Inputs/AuthSocialButton";
import Button from "./Inputs/Button";
import Input from "./Inputs/Input";

enum FormVariants {
	LOGIN = "LOGIN",
	REGISTER = "REGISTER",
}

export default function AuthForm(): React.JSX.Element {
	const router = useRouter();
	const { data: session } = useSession() as { data: UserSession | undefined };
	const { systemTheme, theme } = useTheme();
	const currentTheme = theme === "system" ? systemTheme : theme;
	const isDark = currentTheme === "dark";
	const [variant, setVariant] = React.useState<FormVariants>(FormVariants.LOGIN);
	const [loading, setLoading] = React.useState<boolean>(false);
	const toggleVariant = React.useCallback(() => {
		if (variant === FormVariants.LOGIN) {
			setVariant(FormVariants.REGISTER);
		} else {
			setVariant(FormVariants.LOGIN);
		}
	}, [variant]);
	const schema = Yup.object().shape({
		password: Yup.string()
			.required("Password is required")
			.min(8, "Password must be at least 8 characters")
			.matches(
				/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,}$/,
				"Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
			),
		email: Yup.string().email("Email is invalid").required("Email is required"),
		name: variant === FormVariants.REGISTER ? Yup.string().required("Name is required") : Yup.string(),
	});
	const {
		register,
		handleSubmit,
		reset,
		formState: { errors },
	} = useForm({
		mode: "onChange",
		resolver: yupResolver(schema),
		defaultValues: {
			name: "",
			email: "",
			password: "",
		},
	});
	const onSubmit: SubmitHandler<FieldValues> = (data: FieldValues) => {
		if (loading) return;
		setLoading(true);
		if (variant === FormVariants.LOGIN) {
			signIn("credentials", {
				...data,
				redirect: false,
			})
				.then((callback) => {
					if (callback?.error) {
						toast.error("Invalid credentials");
					}
					if (callback?.ok && !callback.error) {
						toast.success("Logged in successfully");
					}
				})
				.finally(() => {
					setLoading(false);
				});
		} else {
			void axios
				.post("/api/register", data)
				.then(() => {
					void signIn("credentials", {
						...data,
						redirect: false,
					}).then((callback) => {
						if (callback?.error) {
							toast.error("Invalid credentials");
						}
						if (callback?.ok && !callback.error) {
							toast.success("Registered successfully!");
							router.push("/chat");
						}
					});
				})
				.catch((err) => {
					toast.error(String(err.response.data.error));
				})
				.finally(() => {
					setLoading(false);
				});
		}
	};
	const socialAction = (action: string): void => {
		if (loading) return;
		setLoading(true);
		if (session) {
			void signOut();
		}
		void signIn(action, {
			callbackUrl: "/chat",
		})
			.then((callback) => {
				if (callback?.error) {
					toast.error("Invalid credentials");
				}
				if (callback?.ok && !callback.error) {
					toast.success("Logged in successfully");
				}
			})
			.finally(() => {
				setLoading(false);
			});
	};
	React.useEffect(() => {
		reset();
		setLoading(false);
	}, [variant, reset]);
	React.useEffect(() => {
		if (session) {
			setLoading(true);
			const email = session.user?.email ?? "";
			void axios
				.get("/api/users", {
					params: {
						email,
					},
				})
				.then((res) => {
					if (res.status === 200) {
						if (res.data.emailVerified) {
							router.push("/chat");
						} else {
							router.push("/verify");
						}
					}
				});
			setLoading(false);
		}
	}, [session, router]);
	return (
		<>
			{session ? (
				<Lottie animationData={animationData} loop={true} height={500} width={500} />
			) : (
				<div className="flex w-full h-full flex-col md:flex-row">
					<div className="flex flex-1 flex-col justify-center px-8 py-12 sm:px-16 md:px-20 lg:px-24">
						<h2 className="mb-8 text-[28px] font-light text-[#41525d] dark:text-[#e9edef]">
							{variant === FormVariants.LOGIN ? "To use WhatsApp Clone on your computer:" : "Create a new account"}
						</h2>
						{/* eslint-disable-next-line @typescript-eslint/no-misused-promises */}
						<form className="space-y-6" onSubmit={handleSubmit(onSubmit)}>
							{variant === FormVariants.REGISTER && (
								<Input
									disabled={loading}
									register={register}
									errors={errors}
									required
									id="name"
									label="Display Name"
								/>
							)}
							<Input
								disabled={loading}
								register={register}
								errors={errors}
								required
								id="email"
								label="Email Address"
								type="email"
							/>
							<Input
								disabled={loading}
								register={register}
								errors={errors}
								required
								id="password"
								label="Password"
								type="password"
							/>
							<div className="pt-2">
								<Button disabled={loading} fullWidth type="submit">
									{loading ? (
										<BeatLoader color="#fff" className="mx-auto block" size={8} />
									) : variant === FormVariants.LOGIN ? (
										"Sign in"
									) : (
										"Sign up"
									)}
								</Button>
							</div>
						</form>
						<div className="mt-8">
							<div className="relative">
								<div className="absolute inset-0 flex items-center">
									<div className="w-full border-t border-gray-200 dark:border-gray-700" />
								</div>
								<div className="relative flex justify-center text-[13px]">
									<span className="bg-white px-4 text-gray-500 dark:bg-[#222e35] dark:text-gray-400">
										Or continue with
									</span>
								</div>
							</div>
							<div className="mt-6 flex gap-4">
								<AuthSocialButton
									className="bg-gray-50 text-black hover:bg-gray-100 dark:bg-gray-800 dark:text-white dark:hover:bg-gray-700"
									icon={BsGithub}
									onClick={(): void => socialAction("github")}
								/>
								<AuthSocialButton
									className="bg-gray-50 hover:bg-gray-100 dark:bg-gray-800 dark:hover:bg-gray-700"
									icon={FcGoogle}
									onClick={(): void => socialAction("google")}
								/>
							</div>
						</div>
						<div className="mt-8 flex justify-center gap-2 text-[14px] text-gray-600 dark:text-gray-400">
							<div>
								{variant === FormVariants.LOGIN ? "Don't have an account?" : "Already have an account?"}
							</div>
							<div onClick={toggleVariant} className="cursor-pointer font-medium text-[#00a884] hover:underline">
								{variant === FormVariants.LOGIN ? "Sign up" : "Log in"}
							</div>
						</div>
					</div>
					
					{/* Desktop visual placeholder mimicking the QR code section of WA Web */}
					<div className="hidden flex-1 flex-col items-center justify-center border-l border-gray-200 bg-[#f9f9fa] p-12 dark:border-gray-700 dark:bg-[#111b21] md:flex">
						<div className="mb-10 flex h-64 w-64 items-center justify-center rounded-xl bg-white p-4 shadow-sm dark:bg-gray-800 dark:shadow-none">
							<Image src="/logo.png" alt="WhatsApp QR" width={180} height={180} className="opacity-90 dark:opacity-80" />
						</div>
						<h3 className="mb-4 text-center text-xl font-medium text-[#41525d] dark:text-[#e9edef]">
							Fast, simple, secure messaging
						</h3>
						<p className="max-w-[300px] text-center text-[#8696a0]">
							Sign in or create an account to start chatting with your friends and family.
						</p>
					</div>
				</div>
			)}
			<ToastContainer
				position="top-center"
				autoClose={5000}
				closeOnClick
				pauseOnFocusLoss
				theme={isDark ? "dark" : "light"}
			/>
		</>
	);
}
