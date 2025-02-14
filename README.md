# TagMuh - On-Demand Service App

## Overview

**TagMuh** is an on-demand service app designed to connect customers with professionals for various services, similar to platforms like UrbanClap. The app allows users to search for services, hire professionals, chat with them, and manage their profiles. It also provides two distinct modes: one for **service providers** (professionals) and another for **customers** (users seeking services).

Service providers can view their clients, manage chats, and receive notifications, while customers can browse profiles, chat with providers, and hire them directly for services. Built with **Android**, **Firebase**, and **XML**, the app ensures seamless communication, secure transactions, and real-time notifications.

## Features

### Customer Mode
- **Browse Services**: Search and explore a wide range of services posted by professionals.
- **Hire Service Providers**: Hire professionals by viewing their profiles and selecting the desired services.
- **Chat with Providers**: Real-time chat functionality to communicate with service providers for better clarity.
- **Manage Profile**: Customers can manage their personal details and view previous interactions with service providers.
- **Notifications**: Real-time push notifications about service updates, chats, and service bookings.

### Service Provider Mode
- **View Clients**: Service providers can view their client details, including services requested and completed.
- **Manage Chats**: Real-time chat functionality for smooth communication with customers.
- **Receive Notifications**: Service providers are notified of incoming service requests, chats, and bookings.
- **Profile Management**: Providers can update their service offerings, availability, and other professional details.
- **Bookings & Status**: Providers can manage ongoing bookings, and update their availability status.

### Common Features
- **Real-Time Chat**: Instant communication between customers and service providers.
- **Profile Management**: Both customers and service providers can manage their personal and professional profiles.
- **Push Notifications**: Firebase Messaging API is used to send push notifications for chats, bookings, and status updates.
- **Authentication**: Firebase authentication allows users to securely sign up and log in to the app.
- **Tokens**: The app uses tokens for secure communication and to ensure that notifications are delivered to the correct devices.

## Tech Stack

- **Android**: The app is built for Android, providing a smooth and native mobile experience.
- **Java**: Used as the primary programming language for the app’s functionality.
- **XML**: For building the app’s user interface and layout.
- **Firebase**: Integrated for real-time data storage, user authentication, and messaging notifications.
- **Firebase Messaging API**: Used for sending real-time chat notifications and updates.
- **Volley & Retrofit**: APIs are consumed via Volley and Retrofit for network operations and fetching data.
- **Push Notifications**: Firebase Cloud Messaging (FCM) used for sending push notifications to customers and service providers.

## Installation

To run the app locally, follow the steps below:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/tagmuh-app.git
