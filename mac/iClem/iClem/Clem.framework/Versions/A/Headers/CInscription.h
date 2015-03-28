//
//  CInscription.h
//  Clem
//
//  Created by Odric Roux-Paris on 09/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CJSONSerializer.h"
#import "CJSONDeserializer.h"
#import "CRequest.h"
typedef void (^CEndInscription)(NSString *username, NSString *mail, NSError *error);
@interface CInscription : NSObject
/*
 Variables permettant la création d'un compte
 */
@property NSString *username;
@property NSString *password;
@property NSString *mail;

/*
 Variables permettant la requète
 */
@property (strong) CRequest *request;
@property (copy, nonatomic) CEndInscription bloc;


/*
 Méthode
 */
- (id)initWithUsername:(NSString *)name password:(NSString *)pass EMail:(NSString *)eMail;
+ (instancetype)createUserWithUsername:(NSString *)name password:(NSString *)pass EMail:(NSString *)eMail;

- (void)createUserAsBloc:(CEndInscription )endBloc;

@end
