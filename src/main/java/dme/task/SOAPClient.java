package dme.task;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.soap.*;
import java.util.Iterator;

public class SOAPClient {

    private String INN = null;
    private String KPP = null;
    private String DATE = null;
    private String StateInfoAgent = null;
    private int State = -1;
    private boolean AgentUL = false;

    private String namespaceURI = null;
    private String soapUrl = null;
    private String serviceName = null;
    private String namespace = null;
    private String soapAction = null;

    public SOAPClient(String inn, String kpp, String date) {
        INN = inn;
        KPP = kpp;
        DATE = date;
        AgentUL = true;
        setSoapParams();
        callSoapWebService(soapUrl, soapAction);
    }

    public SOAPClient(String inn, String date) {
        INN = inn;
        DATE = date;
        AgentUL = false;
        setSoapParams();
        callSoapWebService(soapUrl, soapAction);
    }

    private void setSoapParams() {
        namespaceURI = "http://ws.unisoft/FNSNDSCAWS2/Request";
        soapUrl = "https://npchk.nalog.ru:443/FNSNDSCAWS_2";
        serviceName = "NdsRequest2";
        namespace = "ns";
        soapAction = "NdsRequest2";
    }

    private void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(namespace, namespaceURI);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem, soapBodyElem1;
        Name INNname, KPPname, DATEname;
        soapBodyElem = soapBody.addChildElement(serviceName, namespace);
        soapBodyElem1 = soapBodyElem.addChildElement("NP", namespace);
        if (KPP != null) {
            DATEname = envelope.createName("DT");
            KPPname = envelope.createName("KPP");
            INNname = envelope.createName("INN");
            soapBodyElem1.addAttribute(DATEname, DATE);
            soapBodyElem1.addAttribute(KPPname, KPP);
            soapBodyElem1.addAttribute(INNname, INN);
        } else {
            DATEname = envelope.createName("DT");
            INNname = envelope.createName("INN");
            soapBodyElem1.addAttribute(DATEname, DATE);
            soapBodyElem1.addAttribute(INNname, INN);
        }
    }

    private SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        soapMessage.saveChanges();

        // Печать XML текста запроса
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }

    private void callSoapWebService(String destination, String soapAction) {
        SOAPConnectionFactory soapFactory = null;
        SOAPConnection soapConnect = null;
        SOAPMessage soapRequest = null;
        SOAPMessage soapResponse = null;
        try {
            // Создание SOAP Connection
            soapFactory = SOAPConnectionFactory.newInstance();
            soapConnect = soapFactory.createConnection();

            // Создание SOAP Message для отправки
            soapRequest = createSOAPRequest(soapAction);
            // Получение SOAP Message
            soapResponse = soapConnect.call(soapRequest, destination);

            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();
            getContentResponse(soapResponse);
            soapConnect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getContentResponse(SOAPMessage soapResponse) throws SOAPException {
        Iterator iterator = soapResponse.getSOAPBody().getChildElements();
        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();
            if (node.hasChildNodes()) {
                Node childNode = node.getFirstChild();
                if (childNode.getNodeType() == childNode.ELEMENT_NODE) {
                    Element element = (Element) childNode;
                    State = Integer.parseInt(element.getAttribute("State"));
                }
            }
        }
    }

    public String getStateInfo() {
            switch (State) {
                case 0:
                    StateInfoAgent = ResponseUL.OTVET0.getTitle();
                    break;
                case 1:
                    StateInfoAgent = ResponseUL.OTVET1.getTitle();
                    break;
                case 2:
                    StateInfoAgent = ResponseUL.OTVET2.getTitle();
                    break;
                case 3:
                    StateInfoAgent = ResponseUL.OTVET3.getTitle();
                    break;
                case 4:
                    StateInfoAgent = ResponseUL.OTVET4.getTitle();
                    break;
                case 5:
                    StateInfoAgent = ResponseUL.OTVET5.getTitle();
                    break;
                case 6:
                    StateInfoAgent = ResponseUL.OTVET6.getTitle();
                    break;
                case 7:
                    StateInfoAgent = ResponseUL.OTVET7.getTitle();
                    break;
                case 8:
                    StateInfoAgent = ResponseUL.OTVET8.getTitle();
                    break;
                case 9:
                    StateInfoAgent = ResponseUL.OTVET9.getTitle();
                    break;
                case 11:
                    StateInfoAgent = ResponseUL.OTVET11.getTitle();
                    break;
                case 12:
                    StateInfoAgent = ResponseUL.OTVET12.getTitle();
                    break;
            }
        return StateInfoAgent;
    }

    int getState() {
        return State;
    }
}

enum ResponseUL {
    OTVET0 ("Налогоплательщик зарегистрирован в ЕГРН и имел статус действующего в указанную дату"),
    OTVET1 ("Налогоплательщик зарегистрирован в ЕГРН, но не имел статус действующего в указанную дату"),
    OTVET2 ("Налогоплательщик зарегистрирован в ЕГРН"),
    OTVET3 ("Налогоплательщик с указанным ИНН зарегистрирован в ЕГРН, КПП не соответствует ИНН или не указан"),
    OTVET4 ("Налогоплательщик с указанным ИНН не зарегистрирован в ЕГРН"),
    OTVET5 ("Некорректный ИНН"),
    OTVET6 ("Недопустимое количество символов ИНН"),
    OTVET7 ("Недопустимое количество символов КПП"),
    OTVET8 ("Недопустимые символы в ИНН"),
    OTVET9 ("Недопустимые символы в КПП"),
    OTVET11 ("некорректный формат даты"),
    OTVET12 ("некорректная дата (ранее 01.01.1991 или позднее текущей даты)");

    private String title;

    ResponseUL(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
