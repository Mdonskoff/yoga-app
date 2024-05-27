
import { expect } from '@jest/globals';
import { TeacherService } from './teacher.service';
import {of } from 'rxjs';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpClientSpy: any;

  beforeEach(() => {
    httpClientSpy = {
      get : jest.fn(),
    }
    service = new TeacherService(httpClientSpy);
    
  });
  afterEach(() => {
    // restore the spy created with spyOn
    jest.restoreAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it(' should get all teatcher', () => {
      const res = "Test";
      const url = 'api/teacher';
      jest.spyOn(httpClientSpy, 'get').mockReturnValue(of(res));

      service.all()

      expect(httpClientSpy.get).toBeCalledTimes(1);
      expect(httpClientSpy.get).toHaveBeenCalledWith(url);
  
  })

  it(' should get detail teatcher', (done) => {
    const res = "Test";
    const url = 'api/teacher/1';
    let id : string = '1';
    jest.spyOn(httpClientSpy, 'get').mockReturnValue(of(res));
    
    service.detail(id).subscribe(
      {
        next: data => {
          expect(data).toEqual("Test");
          done()
        }
      }
    );

    expect(httpClientSpy.get).toBeCalledTimes(1);
    expect(httpClientSpy.get).toHaveBeenCalledWith(url);
})
});
