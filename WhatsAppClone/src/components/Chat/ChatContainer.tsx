"use client";

import { useSession } from "next-auth/react";
import React from "react";
import { useRecoilState } from "recoil";

import { UserSession } from "@/lib/model";
import { FullConversationType, FullMessageType } from "@/lib/types";

import { messageSearch } from "../atoms/messageSearch";
import ChatHeader from "./ChatHeader";
import Empty from "./Empty";
import MessageBar from "./MessageBar";
import MessageContainer from "./MessageContainer";

export default function ChatContainer({
	conversation,
	messages,
}: {
	conversation: FullConversationType | null;
	messages: FullMessageType[];
}): React.JSX.Element {
	const { data: session } = useSession() as { data: UserSession | undefined };
	const MessageSearch = useRecoilState(messageSearch)[0];
	return (
		<>
			{conversation ? (
				<div
					className={`z-15 flex h-full w-full flex-col items-center bg-[#efeae2] dark:bg-[#0b141a] ${
						MessageSearch ? "hidden lg:flex" : "flex"
					}`}>
					<ChatHeader conversation={conversation} email={session?.user?.email ?? ""} />
					<MessageContainer
						users={conversation.users}
						id={conversation.id}
						// eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
						messages={messages?.length ? messages : []}
						email={session?.user?.email ?? ""}
					/>
					<MessageBar id={conversation.id} />
				</div>
			) : (
				<Empty />
			)}
		</>
	);
}
