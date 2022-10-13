import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

public class MedicalServiceImplTests {

    @MethodSource("source1")
    @ParameterizedTest
    void send_mess_in_checkBloodPressureNotEqualsSource_test(BloodPressure bloodPressure, PatientInfo patientInfo){


        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkBloodPressure(patientInfo.getId(), bloodPressure);
        //Если не равны, то отправить сообщение о помощи
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.anyString());
    }

    public static Stream<Arguments> source1(){
        return Stream.of(Arguments.of(new BloodPressure(130, 80), new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.7), new BloodPressure(120,80)))),
                Arguments.of(new BloodPressure(10, 20), new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.7), new BloodPressure(120,80)))));
    }

    @MethodSource("source2")
    @ParameterizedTest
    void send_mess_in_checkTemperature_test(BigDecimal temperature, PatientInfo patientInfo){

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkTemperature(patientInfo.getId(), temperature);
        //Если не равны, то отправить сообщение о помощи
        Mockito.verify(sendAlertService, Mockito.times(1)).send(Mockito.anyString());


    }

    public static Stream<Arguments> source2(){
        return Stream.of(Arguments.of(new BigDecimal(30), new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.5), new BloodPressure(130,80)))),
                Arguments.of(new BigDecimal(34.9), new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.5), new BloodPressure(10,20)))));
    }

    @MethodSource("source3")
    @ParameterizedTest
    void send_mess_in_checkBloodPressureEqualsSource_test(BloodPressure bloodPressure, BigDecimal temperature, PatientInfo patientInfo){

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById(patientInfo.getId())).thenReturn(patientInfo);

        SendAlertServiceImpl sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoFileRepository, sendAlertService);
        medicalService.checkBloodPressure(patientInfo.getId(), bloodPressure);
        medicalService.checkTemperature(patientInfo.getId(), temperature);
        //Если равны, то не отправлять сообщение о помощи
        Mockito.verify(sendAlertService, Mockito.times(0)).send(Mockito.anyString());
    }

    public static Stream<Arguments> source3(){
        return Stream.of(Arguments.of(new BloodPressure(120, 80),
                        new BigDecimal(36.6),
                        new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.6), new BloodPressure(120,80)))),
                Arguments.of(new BloodPressure(120, 80),
                        new BigDecimal(35.1),
                        new PatientInfo("TestId", "vasya", "111", LocalDate.MIN, new HealthInfo(new BigDecimal(36.6), new BloodPressure(120,80)))));
    }
}
