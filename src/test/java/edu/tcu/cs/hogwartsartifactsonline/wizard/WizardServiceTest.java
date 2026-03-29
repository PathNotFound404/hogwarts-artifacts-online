package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.exceptiton.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>();

        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Albus Dumbledore");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName( "Harry Potter");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Neville Longbottom");
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void testFindByIdSuccess(){
        // Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId(1);
        w.setName("Albus Dumbledore");

        a.setOwner(w);

        given(wizardRepository.findById(1)).willReturn(Optional.of(w));


        // When
        Wizard returnedWizard = wizardService.findById(1);

        // Then
        assertThat(returnedWizard.getId()).isEqualTo(w.getId());
        assertThat(returnedWizard.getName()).isEqualTo(w.getName());
        verify(wizardRepository, times(1)).findById(1);

    }

    @Test
    void testFindByIdNotFound(){
        // Given
        given(wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = wizardService.findById(9);
        });
        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id 9 :(");
        verify(wizardRepository, times(1)).findById(9);
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(wizardRepository.findAll()).willReturn(this.wizards);

        // When
        List<Wizard> actualWizards = wizardService.findAll();

        // Then
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermione Granger");

        given(wizardRepository.save(newWizard)).willReturn(newWizard);

        // When
        Wizard returnedWizard = this.wizardService.save(newWizard);

        // Then
        assertThat(returnedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess(){
        // Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Updated Name");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard updatedWizard = wizardService.update(1, update);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo(update.getName());
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);
    }

    @Test
    void testUpdateNotFound(){
        // Given
        Wizard update = new Wizard();
        update.setName("Updated Name");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());


        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(1, update);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess(){
        // Given
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));
        doNothing().when(this.wizardRepository).deleteById(1);

        // When
        this.wizardService.delete(1);

        // Then
        verify(this.wizardRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
        // Given

        given(this.wizardRepository.findById(5)).willReturn(Optional.empty());


        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(5);
        });


        // Then
        verify(this.wizardRepository, times(1)).findById(5);
    }
}
