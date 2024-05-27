import {screen} from '@testing-library/angular'
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';

describe('RegisterComponent integration test', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService : AuthService;
  let router : Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  })

  const loadSessionFormValue = () => {
    component.form?.get('email')?.setValue('test@test.fr');
    component.form?.get('firstName')?.setValue("Valery");
    component.form?.get('lastName')?.setValue('Grodin');
    component.form?.get('password')?.setValue("password");
  }


  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a user', () => {
      const authServiceSpy = jest.spyOn(authService, 'register');
      loadSessionFormValue();
      const button = fixture.debugElement.query(By.css('button[type="submit"]'));
      fixture.detectChanges();

      expect(button.nativeElement.disabled).toBeFalsy();

      button.nativeElement.click();
      fixture.detectChanges();
      
      expect(authServiceSpy).toHaveBeenCalled();
  })

  it('should disabled submit button when we create a user', () => {
    const authServiceSpy = jest.spyOn(authService, 'register');
    loadSessionFormValue();
    component.form?.get('password')?.setValue("");
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    fixture.detectChanges();

    expect(button.nativeElement.disabled).toBeTruthy();

    button.nativeElement.click();
    fixture.detectChanges();
    
    expect(authServiceSpy).not.toHaveBeenCalled();
  })

});
