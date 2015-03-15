//
//  CToken.h
//  Clem
//
//  Created by Odric Roux-Paris on 10/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CToken : NSObject
@property (readonly) NSString *accessToken;
@property (readonly) NSString *refreshToken;
@property (readonly) NSDate *expireIn;

- (id)initWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireIn:(NSTimeInterval)second;
- (id)initWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireInDate:(NSDate *)d;

- (BOOL)hasExpire;

+ (instancetype)tokenWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireIn:(NSTimeInterval)second;
+ (instancetype)tokenWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireInDate:(NSDate *)d;
+ (void)setDefaultToken:(CToken *)t;
+ (CToken *)defaultToken;



@end

/*
 Variable globale du token
 */
static CToken *defaultToken;

