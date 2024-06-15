/**
 * @jest-environment jsdom
 */
import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed} from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule, NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { FormComponent } from './form.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';
import { By } from '@angular/platform-browser';

describe('FormComponent integration test', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpTestingController :HttpTestingController
  let teacherService : any
  let sessionApiService : SessionApiService

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockTeacher : Teacher[] = [{
    id: 1,
    lastName: "Diana",
    firstName: "Prince",
    createdAt: new Date(),
    updatedAt: new Date()
  }]

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
        NoopAnimationsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },

      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  beforeEach( () => {
    httpTestingController = TestBed.inject(HttpTestingController);
    teacherService = TestBed.inject(TeacherService);
    sessionApiService = TestBed.inject(SessionApiService);
  })

  const loadSessionFormValue = (date : string) => {
    component.sessionForm?.get('name')?.setValue('test');
    component.sessionForm?.get('date')?.setValue(date);
    component.sessionForm?.get('description')?.setValue('description test');
    component.sessionForm?.get('teacher_id')?.setValue(1);
  }

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create a session', () => {
    const nameInput = fixture.nativeElement.querySelector('input[formControlName="name"]');
    const dateInput = fixture.nativeElement.querySelector('input[formControlName="date"]');
    const descriptionInput = fixture.nativeElement.querySelector('textarea[formControlName="description"]');
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    let mockRequest = httpTestingController.expectOne('api/teacher');
    mockRequest.flush(mockTeacher);
    let date = new Date().toISOString().split('T')[0];
    let submitSpy = jest.spyOn(component, 'submit');

    jest.spyOn(sessionApiService, 'create');
    loadSessionFormValue(date);
    fixture.detectChanges();

    expect(nameInput.value).toBe('test');
    expect(dateInput.value).toBe(date);
    expect(descriptionInput.value).toBe('description test');
    expect(button.nativeElement.disabled).toBeFalsy();

    button.nativeElement.click();

    expect(submitSpy).toHaveBeenCalled();
    expect(sessionApiService.create).toHaveBeenCalled();
  });

  it('should update a session', () => {
    const nameInput = fixture.nativeElement.querySelector('input[formControlName="name"]');
    const dateInput = fixture.nativeElement.querySelector('input[formControlName="date"]');
    const descriptionInput = fixture.nativeElement.querySelector('textarea[formControlName="description"]');
    const button = fixture.debugElement.query(By.css('button[type="submit"]'));
    let mockRequest = httpTestingController.expectOne('api/teacher');
    mockRequest.flush(mockTeacher);
    let date = new Date().toISOString().split('T')[0];
    let submitSpy = jest.spyOn(component, 'submit');
    component.onUpdate = true;
    jest.spyOn(sessionApiService, 'update');


    loadSessionFormValue(date);
    fixture.detectChanges();

    expect(nameInput.value).toBe('test');
    expect(dateInput.value).toBe(date);
    expect(descriptionInput.value).toBe('description test');
    expect(button.nativeElement.disabled).toBeFalsy();

    button.nativeElement.click();

    expect(submitSpy).toHaveBeenCalled();
    expect(sessionApiService.update).toHaveBeenCalled();
  });
});
