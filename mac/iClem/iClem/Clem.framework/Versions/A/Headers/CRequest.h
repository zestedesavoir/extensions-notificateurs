//
//  CRequest.h
//  Clem
//
//  Created by Odric Roux-Paris on 07/02/2015.
//  Copyright (c) 2015 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol CRequestDelegate <NSObject>

- (void)requestEnd:(NSString *)reponse;
- (void)requestError:(NSError *)error;


@end



typedef void (^CRequestResultBloc)(NSString *reponse, NSError *error);

@interface CRequest : NSObject<NSURLConnectionDataDelegate>
@property NSMutableData *responseData;
@property NSMutableURLRequest *request;
@property NSURLConnection *connection;

@property NSString *methode;
@property NSString *parameter;
@property (readonly) NSURL *url;
@property (copy, nonatomic) CRequestResultBloc bloc;
@property (nonatomic, strong) NSObject<CRequestDelegate> *delegate;
@property BOOL useToken;
/*
 url : Url 
 méthode : méthode employé pour faire la request (ne peux être nil)
 param : paramètre pour la request (peux être nil)
 */
- (id)initWithURL:(NSURL *)iUrl methode:(NSString *)met parameter:(NSString *)param;
- (id)initWithURL:(NSURL *)iUrl methode:(NSString *)met parameter:(NSString *)param useToken:(BOOL)t;
+ (instancetype)requestWithURL:(NSURL *)iUrl methode:(NSString *)met parameter:(NSString *)param;
+ (instancetype)requestWithURL:(NSURL *)iUrl methode:(NSString *)met parameter:(NSString *)param useToken:(BOOL)t;


- (void)requestWithPost;
- (void)requestWithGet;
- (void)requestWithPut;
- (void)sendRequest;


- (void)sendRequestWithBloc:(CRequestResultBloc)fetchBloc;
- (void)sendRequestWithDelegate:(id<CRequestDelegate>)d;

- (void)cleanup;


@end
