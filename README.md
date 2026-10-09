# DocuChat: Chat with Your Documents

A RAG (Retrieval-Augmented Generation) app. Upload documents, then ask
questions in natural language. Answers come only from your documents and
show which files they were based on. Runs fully locally with Ollama:
no paid APIs, no API keys.

## Tech stack
- Backend: Java 21, Spring Boot 4.1, Spring AI 2.0
- AI: Ollama with `llama3.2` (chat) and `nomic-embed-text` (embeddings)
- Vector store: Spring AI `SimpleVectorStore`, persisted to JSON
- Frontend: React (Vite) and Tailwind CSS

## Architecture
React UI (5173) -> Spring Boot REST API (8080) -> Ollama (11434)

Upload flow:
file -> Tika reader -> TokenTextSplitter (chunks) -> embeddings -> VectorStore

Question flow:
question -> embed -> similarity search (top 3 chunks) -> prompt with context
-> llama3.2 -> answer + sources

## Key components
- `DocumentIngestionService`: read, chunk, embed and store files
- `RagService`: retrieve, augment, generate, collect sources
- `DocumentRegistry`: remembers uploaded file names (data/documents.txt)
- `GlobalExceptionHandler`: friendly errors (503 when Ollama is down)
- `WebConfig`: CORS for the React app

## API
| Method | Path | Purpose |
|---|---|---|
| POST | /documents/upload | Upload a file (form field `file`) |
| GET | /documents | List uploaded files |
| GET | /ask?question=... | Answer with sources |
| GET | /chat?message=...&conversationId=... | Plain chat with memory |
| GET | /chat/stream?message=... | Streaming chat (SSE) |

## Run it
1. Install Ollama, then `ollama pull llama3.2` and `ollama pull nomic-embed-text`
2. Start Ollama: `ollama serve`
3. Backend: run `DocuchatApplication` (http://localhost:8080)
4. Frontend: `cd docuchat-ui`, `npm install`, `npm run dev` (http://localhost:5173)

## Limitations and future work
- SimpleVectorStore is for learning. Production would use PGVector or similar.
- No authentication, and all users share one document set.
- Chat memory is not yet connected to /ask (follow-up questions).
- Streaming exists on the backend but not yet in the UI.
- [Add anything you plan to improve]