# TV Remote Control Android App

This project is an Android TV remote control application designed for hotel room usage.  
The app allows a user to pair their mobile device with a TV using a QR-code–based authorization flow and then send remote control commands securely to a backend service.

---

## Overview

The application follows a simple but secure pairing and authorization model:

- A **QR code** displayed on the TV contains an **opaque token**
- The mobile app scans or receives this token via a **deeplink**
- Each remote control action sends the token to the backend
- The backend validates the token and authorizes the action
- Invalid or expired tokens trigger re-pairing

---

## Architecture

The app follows a **clean, testable architecture** inspired by MVVM and repository patterns.

### Main Layers

UI (Activities + Jetpack Compose)
↓
ViewModel
↓
Use case
↓
Repository
↓
Backend (mocked with Postman)

### Responsibilities

#### UI Layer
- Displays remote control buttons (volume, channel, power, etc.)
- Reacts to ViewModel state (authorized / unauthorized)
- Handles deeplinks for pairing

#### ViewModel
- Holds UI state
- Exposes actions such as `onButtonPressed()`
- Validates presence of a token

#### Use cases
- Encapsulate actions to be performed in repository
- Log errors

#### Repository
- Sends requests to the backend
- Stores and retrieves the token

#### Backend
- Validates tokens
- Authorizes or rejects commands

---

## Pairing & Authorization Flow

### 1. QR Code & Deeplink

The app supports pairing via a deeplink with the following format:

tvremote://pair?token={token}

- The token is an **opaque identifier** (UUID)
- The app extracts the token and stores it securely

#### Special Case: Empty Token

If the app is opened with an **empty token**:

tvremote://pair

Then:
- The stored token is **cleared** from encrypted shared preferences
- The user is considered **unauthorized**
- The app prompts the user to scan a new QR code

#### Testing

In **test_qr_codes** directory, there are 3 generated QR codes to test each case

- **clear_token.png**: clear currently stored token
- **invalid_token.png**: open app with invalid token, each request will fail with unauthorized status code
- **valid_token.png**: open app with valid token, all requests to backend will be successful

---

### 2. Sending Remote Commands

When the user presses any remote control button:

1. The app reads the token from encrypted storage
2. The token is sent to the backend along with the command
3. Backend validates the token:
    - **Valid token** → command is accepted
    - **Invalid token** → command is rejected
4. On rejection, the user is prompted to re-pair by scanning a new QR code

---

## Token Storage & Security

- The token is stored using **EncryptedSharedPreferences**
- The token is never interpreted on the client
- It is treated as an **opaque authorization credential**

This ensures:
- Protection against local data extraction
- No exposure of room or user identifiers
- Easy token invalidation on the backend

---

## Mock Backend

For development and testing purposes, a mock backend is provided.

### Predefined Tokens

- **Valid token**: constant UUID (**c35bcef7-7354-48ca-afcc-5a8b0f7d1a3e**)
- **Invalid token**: constant UUID (**7a333900-bf41-4d15-8c6d-4c3330b7251d**)

Behavior:
- Requests with the valid token always succeed
- Requests with the invalid token always fail

This allows deterministic testing of authorization logic.

---

## Testing Strategy

### Unit Tests

Unit tests focus on **business logic**, not UI:

- ViewModels
- Repositories

Covered scenarios include:
- Token presence and absence
- Valid vs invalid backend responses
- Authorization state transitions

UI components are intentionally excluded from unit tests.

---

## Why Use an Opaque Token?

Using an opaque token for TV remote authorization is intentional and beneficial, especially in a **hotel room context**.

### Reasons

- **Security**:  
  The token contains no meaningful data (room number, guest info, etc.)

- **Privacy**:  
  The backend controls the mapping between token and TV/room

- **Revocability**:  
  Tokens can be invalidated instantly (e.g., guest checkout)

- **Simplicity**:  
  No need for user accounts or authentication flows

- **Isolation**:  
  Each room/session has its own token, preventing cross-room control

This makes the solution scalable, secure, and guest-friendly.

---

## Possible Improvements

The current implementation is intentionally minimal. Future improvements may include:

### Testing
- Add **instrumentation tests** for UI and integration flows
- Increase unit test coverage, especially for **use cases / domain logic**

### UI & UX
- Replace simple text buttons with **icons** and visual controls
- Improve layout for better usability on different screen sizes

### Pairing Experience
- Open the **camera directly** instead of showing a “close app and scan” prompt
- Provide an in-app QR scanning flow
- Better error messaging for expired or invalid tokens

---

## Summary

This app demonstrates a clean, secure, and testable approach to building a TV remote control application using:

- QR-code–based pairing
- Opaque token authorization
- Encrypted local storage
- Mockable backend
- Unit-tested business logic

The architecture is designed to scale while keeping the user experience simple and secure.
