import { expect } from '@jest/globals';
import { UserService } from './user.service';
import { of } from 'rxjs';

describe('UserService', () => {
  let service: UserService;
  let httpClientSpy: any;

  beforeEach(() => {
     httpClientSpy = {
      get : jest.fn(),
      delete : jest.fn()
    }
    service = new UserService(httpClientSpy);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get the user by id', () => {
    const pathService = 'api/user/1';
    const id : string = '1';
    jest.spyOn(httpClientSpy, 'get').mockReturnValue(of("res"));

    service.getById(id);

    expect(httpClientSpy.get).toHaveBeenCalledWith(pathService);
  })

  it('should delete the user by id', () => {
    const pathService = 'api/user/1';
    const id : string = '1';
    jest.spyOn(httpClientSpy, 'delete').mockReturnValue(of("res"));

    service.delete(id);

    expect(httpClientSpy.delete).toHaveBeenCalledWith(pathService);
  })
});
