//
//  CToken.m
//  Clem
//
//  Created by Odric Roux-Paris on 10/03/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import "CToken.h"
#import <SSKeychain/SSKeychain.h>
@implementation CToken
@synthesize accessToken, refreshToken, expireIn;

#pragma mark - Méthode init
- (id)initWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireIn:(NSTimeInterval)second{
    self = [super init];
    
    if (self){
        accessToken = aToken;
        refreshToken = r;
        expireIn = [[NSDate date] dateByAddingTimeInterval:second];
    }
    
    return self;
}
- (id)initWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireInDate:(NSDate *)d{
    self = [super init];
    
    if (self){
        accessToken = aToken;
        refreshToken = r;
        expireIn = d;
    }
    
    return self;
}

- (BOOL)hasExpire{
    return ([[self expireIn] compare:[NSDate date]] == NSOrderedDescending) ? YES:NO;
}

- (NSString *)description{
    return [NSString stringWithFormat:@"AcessToken: %@, refreshToken: %@", accessToken, refreshToken];
}


+ (instancetype)tokenWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireInDate:(NSDate *)d{
    return [[[self class] alloc] initWithAcessToken:aToken refreshToken:r expireInDate:d];
}
+ (instancetype)tokenWithAcessToken:(NSString *)aToken refreshToken:(NSString *)r expireIn:(NSTimeInterval)second{
    return [[[self class] alloc] initWithAcessToken:aToken refreshToken:r expireIn:second];
}

+ (CToken *)defaultToken{
    return defaultToken;
}
+ (void)setDefaultToken:(CToken *)t{
    if (defaultToken){
        defaultToken = nil;
    }
    
    defaultToken = t;
    
}





@end
