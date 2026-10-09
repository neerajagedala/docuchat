const BASE_URL = "http://localhost:8080";

export async function askQuestion(question) {
  const response = await fetch(
    `${BASE_URL}/ask?question=${encodeURIComponent(question)}`,
  );
  const data = await response.json();

  if (!response.ok) {
    throw new Error(data.error || "Something went wrong.");
  }
  return data; // { answer, sources }
}
export async function listDocuments() {
  const response = await fetch(`${BASE_URL}/documents`);
  if (!response.ok) {
    throw new Error("Could not load documents.");
  }
  return response.json(); // ["file1.txt", "file2.pdf"]
}

export async function uploadDocument(file) {
  const formData = new FormData();
  formData.append("file", file);

  const response = await fetch(`${BASE_URL}/documents/upload`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    let message = "Upload failed.";
    try {
      const data = await response.json();
      message = data.error || message;
    } catch {
      // response was not JSON, keep the default message
    }
    throw new Error(message);
  }
  return response.text(); // "Uploaded '...' and created N chunks."
}
