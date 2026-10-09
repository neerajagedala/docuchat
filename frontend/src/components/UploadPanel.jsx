import { useState } from "react";
import { uploadDocument } from "../api/docuchatApi";

function UploadPanel({ onUploaded }) {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [status, setStatus] = useState(null); // { type: "success" | "error", text }

  async function handleUpload() {
    if (!file) return;

    setLoading(true);
    setStatus(null);
    try {
      const message = await uploadDocument(file);
      setStatus({ type: "success", text: message });
      onUploaded(file.name);
      setFile(null);
    } catch (err) {
      setStatus({ type: "error", text: err.message });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <h2 className="text-sm font-semibold uppercase tracking-wide text-slate-500">
        Upload document
      </h2>

      <label className="mt-3 flex cursor-pointer flex-col items-center justify-center rounded-xl border-2 border-dashed border-slate-300 bg-slate-50 px-4 py-6 text-center transition hover:border-indigo-400 hover:bg-indigo-50">
        <span className="text-sm font-medium text-slate-700">
          {file ? file.name : "Click to choose a file"}
        </span>
        <span className="mt-1 text-xs text-slate-400">PDF, DOCX or TXT</span>
        <input
          type="file"
          accept=".pdf,.docx,.txt"
          className="hidden"
          onChange={(e) => setFile(e.target.files[0] || null)}
        />
      </label>

      <button
        onClick={handleUpload}
        disabled={!file || loading}
        className="mt-3 w-full rounded-lg bg-indigo-600 px-4 py-2 text-sm font-medium text-white shadow-sm transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:bg-slate-300"
      >
        {loading ? "Uploading..." : "Upload"}
      </button>

      {status && (
        <p
          className={`mt-3 rounded-lg px-3 py-2 text-xs ${
            status.type === "success"
              ? "bg-green-50 text-green-700"
              : "bg-red-50 text-red-700"
          }`}
        >
          {status.text}
        </p>
      )}
    </div>
  );
}

export default UploadPanel;
