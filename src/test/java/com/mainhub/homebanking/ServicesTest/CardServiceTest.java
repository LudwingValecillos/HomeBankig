//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.DTO.NewCardDTO;
//import com.mainhub.homebanking.models.Card;
//import com.mainhub.homebanking.models.Client;
//import com.mainhub.homebanking.models.type.CardColor;
//import com.mainhub.homebanking.models.type.CardType;
//import com.mainhub.homebanking.repositories.CardRepository;
//import com.mainhub.homebanking.repositories.ClientRepository;
//import com.mainhub.homebanking.services.implement.CardServicesImple;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.security.core.Authentication;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Set;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.*;
//
//public class CardServiceTest {
//
//    private CardServicesImple cardServices;
//    private CardRepository cardRepository;
//    private ClientRepository clientRepository;
//
//
//
//    @Test
//    void testValidateNewCardDto_WhenTypeIsBlank_ShouldReturnErrorMessage() {
//        NewCardDTO newCardDTO = new NewCardDTO("", "GOLD");
//
//        String validationMessage = cardServices.validateNewCardDto(newCardDTO);
//
//        assertThat(validationMessage, is("The 'type' field is required."));
//    }
//
//    @Test
//    void testValidateNewCardDto_WhenColorIsBlank_ShouldReturnErrorMessage() {
//        NewCardDTO newCardDTO = new NewCardDTO("CREDIT", "");
//
//        String validationMessage = cardServices.validateNewCardDto(newCardDTO);
//
//        assertThat(validationMessage, is("The 'color' field is required."));
//    }
//
//    @Test
//    void testValidateNewCardDto_WhenTypeAndColorAreProvided_ShouldReturnNull() {
//        NewCardDTO newCardDTO = new NewCardDTO("DEBIT", "RED");
//
//        String validationMessage = cardServices.validateNewCardDto(newCardDTO);
//
//        assertThat(validationMessage, is(nullValue()));
//    }
//
//    @Test
//    void testValidateDetailsCard_WhenClientHasThreeDebitCards_ShouldReturnErrorMessage() {
//        Client client = mock(Client.class);
//        NewCardDTO newCardDTO = new NewCardDTO("DEBIT", "TITANIUM");
//
//        when(client.getCards()).thenReturn( Set.of(
//                new Card(LocalDateTime.now().plusYears(5),CardType.DEBIT, CardColor.GOLD),
//                new Card(LocalDateTime.now().plusYears(5),CardType.DEBIT, CardColor.SILVER),
//                new Card(LocalDateTime.now().plusYears(5),CardType.DEBIT, CardColor.TITANIUM)
//        ));
//
//        String validationMessage = cardServices.validateDetailsCard(client, newCardDTO);
//
//        assertThat(validationMessage, is("You can't have more than 3 debit cards"));
//    }
//
//    @Test
//    void testValidateColor_WhenClientHasCardWithSameColor_ShouldReturnErrorMessage() {
//        Client client = mock(Client.class);
//        NewCardDTO newCardDTO = new NewCardDTO("DEBIT", "GOLD");
//
//        when(client.getCards()).thenReturn(Set.of(
//                new Card(LocalDateTime.now().plusYears(5),CardType.DEBIT, CardColor.GOLD)
//        ));
//
//        String validationMessage = cardServices.validateColor(client, CardColor.GOLD, CardType.DEBIT);
//
//        assertThat(validationMessage, is("You already have a debit card with this color"));
//    }
//
//    @Test
//    void testGenerateCard_ShouldReturnCardWithCorrectProperties() {
//        NewCardDTO newCardDTO = new NewCardDTO("CREDIT", "GOLD");
//
//        Card card = cardServices.generateCard(newCardDTO);
//
//        assertThat(card.getType(), is(CardType.CREDIT));
//        assertThat(card.getColor(), is(CardColor.GOLD));
//        assertThat(card.getThruDate(), is(notNullValue()));
//    }
//
//    @Test
//    void testGetClient_ShouldReturnCorrectClient() {
//        Authentication authentication = mock(Authentication.class);
//        Client client = new Client();
//
//        when(authentication.getName()).thenReturn("test@example.com");
//        when(clientRepository.findByEmail("test@example.com")).thenReturn(client);
//
//        Client foundClient = cardServices.getClient(authentication);
//
//        assertThat(foundClient, is(client));
//    }
//}
