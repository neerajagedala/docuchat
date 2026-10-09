import { useEffect, useState } from "react";
import UploadPanel from "./components/UploadPanel";
import ChatWindow from "./components/ChatWindow";
import { listDocuments } from "./api/docuchatApi";
function App() {
  const [documents, setDocuments] = useState([]);
  useEffect(() => {
    listDocuments()
      .then(setDocuments)
      .catch(() => {
        // backend not reachable yet, keep the list empty
      });
  }, []);

  function handleUploaded(fileName) {
    setDocuments((prev) =>
      prev.includes(fileName) ? prev : [...prev, fileName],
    );
  }

  return (
    <div className="flex h-screen bg-slate-50">
      {/* Sidebar */}
      <aside className="flex w-80 shrink-0 flex-col border-r border-slate-200 bg-white">
        <div className="flex items-center gap-3 border-b border-slate-200 px-5 py-4">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-indigo-600 text-sm font-bold text-white">
            D
          </div>
          <div>
            <h1 className="text-base font-semibold leading-tight text-slate-800">
              DocuChat
            </h1>
            <p className="text-xs text-slate-400">Chat with your documents</p>
          </div>
        </div>

        <div className="space-y-6 overflow-y-auto p-5">
          <UploadPanel onUploaded={handleUploaded} />

          <div>
            <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-500">
              Your documents
            </h2>
            {documents.length === 0 ? (
              <p className="mt-3 text-xs text-slate-400">No documents yet.</p>
            ) : (
              <ul className="mt-3 space-y-2">
                {documents.map((name) => (
                  <li
                    key={name}
                    className="truncate rounded-lg bg-slate-50 px-3 py-2 text-sm text-slate-700"
                  >
                    {name}
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      </aside>

      {/* Chat area */}
      <main className="min-w-0 flex-1">
        <ChatWindow />
      </main>
    </div>
  );
}

export default App;
