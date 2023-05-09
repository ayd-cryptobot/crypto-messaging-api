package com.ufps.cryptobot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import com.ufps.cryptobot.provider.telegram.Provider;
import com.ufps.cryptobot.provider.telegram.contract.ImageMessage;
import com.ufps.cryptobot.provider.telegram.contract.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoBotApplicationTests {

    @Mock
    private TelegramBot telegramBotMock;

    @InjectMocks
    private Provider provider;

    @Test
    public void sendMessageSuccessfully() {
        // Arrange
        Message message = new Message(2345234626L, "Hello, world!");
        Keyboard keyboard = null;
        SendMessage expectedSendMessage = new SendMessage(message.getChat().getId(), message.getText());

        when(this.telegramBotMock.execute(any(SendMessage.class))).thenReturn(mock(SendResponse.class));

        // Act
        SendResponse result = this.provider.sendMessage(message, keyboard);

        // Assert
        verify(this.telegramBotMock, times(1)).execute(any(SendMessage.class));
    }

    @Test
    public void sendImageMessageSuccessfully() {
        // Arrange
        ImageMessage imageMessage = new ImageMessage(23452345L, "", "");

        when(this.telegramBotMock.execute(any(SendPhoto.class))).thenReturn(mock(SendResponse.class));

        // Act
        SendResponse result = this.provider.sendImageMessage(imageMessage);

        // Assert
        verify(this.telegramBotMock, times(1)).execute(any(SendPhoto.class));
    }

}

