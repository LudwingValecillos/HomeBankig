//package com.mainhub.homebanking.ServicesTest;
//
//import com.mainhub.homebanking.DTO.ClientDTO;
//import com.mainhub.homebanking.models.Client;
//import com.mainhub.homebanking.repositories.ClientRepository;
//import com.mainhub.homebanking.services.implement.ClientServicesImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//public class ClientServiceTest {
//
//    @InjectMocks
//    private ClientServicesImpl clientServices;
//
//    @Mock
//    private ClientRepository clientRepository;
//
//    private Client client1;
//    private Client client2;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        client1 = new Client(/* inicializa los atributos del cliente 1 */);
//        client2 = new Client(/* inicializa los atributos del cliente 2 */);
//
//        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));
//        when(clientRepository.findById(1L)).thenReturn(Optional.of(client1));
//        when(clientRepository.findById(2L)).thenReturn(Optional.of(client2));
//    }
//
//    @Test
//    public void testGetAllClient() {
//        List<Client> clients = clientServices.getAllClient();
//        assertThat(clients, hasSize(2));
//        assertThat(clients, contains(client1, client2));
//    }
//
//    @Test
//    public void testGetAllClientDto() {
//        List<ClientDTO> clientDTOs = clientServices.getAllClientDto();
//        assertThat(clientDTOs, hasSize(2));
//        assertThat(clientDTOs.get(0), hasProperty("id", is(client1.getId())));
//        assertThat(clientDTOs.get(1), hasProperty("id", is(client2.getId())));
//    }
//
//    @Test
//    public void testGetClientById() {
//        Client client = clientServices.getClientById(1L);
//        assertThat(client, is(notNullValue()));
//        assertThat(client, is(client1));
//    }
//
//    @Test
//    public void testGetClientDto() {
//        ClientDTO clientDTO = clientServices.getClientDto(client1);
//        assertThat(clientDTO, is(notNullValue()));
//        assertThat(clientDTO, hasProperty("id", is(client1.getId())));
//    }
//}
