function ChatMessage({ message }) {
  const isUser = message.role === "user";

  return (
    <div className={`flex ${isUser ? "justify-end" : "justify-start"}`}>
      <div
        className={`max-w-[80%] rounded-2xl px-4 py-3 text-sm shadow-sm ${
          isUser
            ? "rounded-br-sm bg-indigo-600 text-white"
            : message.isError
              ? "rounded-bl-sm border border-red-200 bg-red-50 text-red-700"
              : "rounded-bl-sm border border-slate-200 bg-white text-slate-800"
        }`}
      >
        <p className="whitespace-pre-wrap">{message.text}</p>

        {message.sources && message.sources.length > 0 && (
          <div className="mt-3 flex flex-wrap items-center gap-2 border-t border-slate-100 pt-2">
            <span className="text-xs text-slate-400">Sources:</span>
            {message.sources.map((source) => (
              <span
                key={source}
                className="rounded-full bg-indigo-50 px-2 py-0.5 text-xs font-medium text-indigo-600"
              >
                {source}
              </span>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default ChatMessage;
