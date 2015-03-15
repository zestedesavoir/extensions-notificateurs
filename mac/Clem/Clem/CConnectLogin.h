//
//  CConnectLogin.h
//  Clem
//
//  Created by Odric Roux-Paris on 08/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Clem.h"
#import "CToken.h"
/*
 Les variables si contre seront utilisées pour accéder aux informations essentiels
 */

typedef void (^CConnectLoginBLoc)(CToken *token ,NSError *error);

//typedef void (^CRequestResultBloc)(NSString *tokenAccess, NSString *tokenRefresh, NSError *error);

@interface CConnectLogin : NSObject


@property (readonly) NSString *username;
@property (readonly) NSString *password;
@property (readonly) NSString *clientID;
@property (readonly) NSString *clientSecrete;

@property (copy, nonatomic) CConnectLoginBLoc bloc;



- (id)initWithUsername:(NSString *)user password:(NSString *)pass clientID:(NSString *)ID clientScrete:(NSString *)secrete;
- (void)connectionZDSWithBloc:(CConnectLoginBLoc)bloc;





@end
