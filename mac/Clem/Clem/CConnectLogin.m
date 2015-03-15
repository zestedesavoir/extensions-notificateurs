//
//  CConnectLogin.m
//  Clem
//
//  Created by Odric Roux-Paris on 08/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CConnectLogin.h"
#import "CToken.h"
#import <AFOAuth2Manager/AFOAuth2Manager.h>
@implementation CConnectLogin
@synthesize username;
@synthesize clientID;
@synthesize clientSecrete;
@synthesize password;
@synthesize bloc;

- (id)initWithUsername:(NSString *)user
              password:(NSString *)pass
              clientID:(NSString *)ID
          clientScrete:(NSString *)secrete{
    
    self = [super init];
    if (self){
        username = user;
        password = pass;
        clientID = ID;
        clientSecrete = secrete;
        
       
    }
    return self;
}
- (void)connectionZDSWithBloc:(CConnectLoginBLoc)resultBloc{
    
    bloc = [resultBloc copy];
    
    NSURL *baseUrl = [NSURL URLWithString:@"https://zestedesavoir.com/"];
    
    AFOAuth2Manager *oauthManager = [AFOAuth2Manager clientWithBaseURL:baseUrl
                                                              clientID:clientID
                                                                secret:clientSecrete];
    
    [oauthManager authenticateUsingOAuthWithURLString:@"oauth2/token/"
                                             username:username
                                             password:password
                                                scope:nil
                                              success:^(AFOAuthCredential *credential) {
                                                  CToken *token = [CToken tokenWithAcessToken:credential.accessToken
                                                                                 refreshToken:credential.refreshToken
                                                                                 expireInDate:credential.expiration];
                                                  [CToken setDefaultToken:token];
                                                  bloc(token, nil);
                                                  
                                              } failure:^(NSError *error) {
                                                  bloc(nil, error);
                                              }];
     

}


@end
