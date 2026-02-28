"use client";

import { AnimatePresence, motion } from "framer-motion";
import React from "react";
import { useRecoilState } from "recoil";

import getConversations from "@/actions/getConversations";
import { FullConversationType } from "@/lib/types";

import { conversationState } from "../atoms/conversationState";
import { sideBarState } from "../atoms/sideBar";
import ChatListHeader from "./ChatListHeader";
import ContactList from "./ContactList";
import List from "./List";

export default function ChatList(): React.JSX.Element {
	const ConversationState = useRecoilState(conversationState)[0];
	const pageType = useRecoilState(sideBarState)[0];
	const [conversations, setConversations] = React.useState<FullConversationType[]>([]);
	React.useEffect(() => {
		async function getData(): Promise<void> {
			const data = await getConversations();
			setConversations(data);
		}
		void getData();
	}, []);
	return (
		<div
			className={`z-20 flex h-full w-full flex-col border-r border-[#d1d7db] bg-white dark:border-[#222e35] dark:bg-[#111b21] ${
				ConversationState ? "hidden" : "flex lg:flex"
			}`}>
			{pageType === "default" && (
				<motion.div>
					<ChatListHeader />
					{conversations.length > 0 && <List conversation={conversations} />}
				</motion.div>
			)}
			<AnimatePresence>{pageType === "contact" && <ContactList />}</AnimatePresence>
		</div>
	);
}
