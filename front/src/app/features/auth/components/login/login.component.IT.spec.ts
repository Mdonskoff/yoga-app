import {screen} from '@testing-library/angular'
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { LoginComponent } from './login.component';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('LoginComponent integration test', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let httpTestingController :HttpTestingController

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        HttpClientTestingModule,
        ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

     beforeEach( () => {
       httpTestingController = TestBed.inject(HttpTestingController);
     })

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should bind form controls to HTML elements', () => {
    const emailInput = fixture.nativeElement.querySelector('input[formControlName="email"]');
    const passwordInput = fixture.nativeElement.querySelector('input[formControlName="password"]');

    expect(emailInput).toBeTruthy();
    expect(passwordInput).toBeTruthy();

    component.form.get('email')?.setValue('test@example.com');
    component.form.get('password')?.setValue('password123');

    expect(emailInput.value).toBe('test@example.com');
    expect(passwordInput.value).toBe('password123');
  });

  it('should login',  async() => {
    const button = fixture.debugElement.query(By.css('button[type="submit"]'))
    const submitSpy = jest.spyOn(component, 'submit');

    component.form.get('email')?.setValue('test@example.com');
    component.form.get('password')?.setValue('password123');
    fixture.detectChanges();

    expect(button.nativeElement.textContent).toContain("Submit");
    button.nativeElement.click();

    expect(submitSpy).toHaveBeenCalled();
    expect(component.onError).toBeFalsy();
      
  });

  it('should not login', async() => {
    const inputEmail  = fixture.debugElement.query(By.css('input[formControlName="email"]'));
    const inputPassword = fixture.debugElement.query(By.css('input[formControlName="password"]'));
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    const submitSpy = jest.spyOn(component, 'submit');


    component.form.get("email")?.setValue("test@example.com");
    component.form.get("password")?.setValue("te");
    fixture.detectChanges();
    
    expect(inputEmail.nativeElement.value).toBe("test@example.com");
    expect(inputPassword.nativeElement.value).toBe("te");
    expect(button.nativeElement.disabled).toBeFalsy();
    
    button.nativeElement.click();
    let mockRequest = httpTestingController.expectOne('api/auth/login');
    mockRequest.flush({error : 'Unauthorized'}, {status : 401, statusText: 'Unauthorized'});

    expect(submitSpy).toHaveBeenCalled();
    expect(component.onError).toBeTruthy();
    fixture.detectChanges();
    expect(screen.getByText("An error occurred").textContent).toBeTruthy();
  });
});
