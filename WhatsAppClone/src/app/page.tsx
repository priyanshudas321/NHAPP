import { Metadata } from "next";
import React from "react";
import Image from "next/image";
import AuthForm from "@/components/AuthForm";
import { meta } from "@/lib/utils";

export const metadata: Metadata = meta;

export default function Home(): React.JSX.Element {
	return (
		<div className="relative flex min-h-screen flex-col items-center overflow-hidden bg-[#d1d7db] dark:bg-[#111b21]">
			<div className="absolute top-0 -z-10 h-[222px] w-full bg-[#00a884] dark:bg-[#111b21]" />

			<div className="z-10 mt-10 w-full max-w-[1000px] md:mt-12">
				<div className="mb-8 flex items-center gap-3 px-4 md:px-0">
					<Image src="/logo.png" alt="WhatsApp" width={32} height={32} className="h-8 w-8 object-contain" />
					<h1 className="text-[14px] font-semibold uppercase tracking-wide text-white">WhatsApp Clone</h1>
				</div>

				<div className="flex min-h-[500px] w-full items-center justify-center overflow-hidden bg-white shadow-[0_17px_50px_0_rgba(11,20,26,.19),_0_12px_15px_0_rgba(11,20,26,.24)] dark:bg-[#222e35] md:rounded-lg">
					<AuthForm />
				</div>
			</div>
		</div>
	);
}
