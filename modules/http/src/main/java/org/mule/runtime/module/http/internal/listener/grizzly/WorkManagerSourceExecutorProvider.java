/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.module.http.internal.listener.grizzly;

import org.mule.runtime.module.http.internal.listener.ServerAddressMap;
import org.mule.service.http.api.server.ServerAddress;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

/**
 * {@link org.mule.runtime.module.http.internal.listener.grizzly.ExecutorProvider} implementation that retrieves an
 * {@link java.util.concurrent.Executor} for a {@link ServerAddress}.
 */
public class WorkManagerSourceExecutorProvider implements ExecutorProvider {

  private ServerAddressMap<Supplier<ExecutorService>> executorPerServerAddress =
      new ServerAddressMap<>(new ConcurrentHashMap<ServerAddress, Supplier<ExecutorService>>());

  /**
   * Adds an {@link java.util.concurrent.Executor} to be used when a request is made to a
   * {@link ServerAddress}
   *
   * @param serverAddress address to which the executor should be applied to
   * @param workManagerSource the executor to use when a request is done to the server address
   */
  public void addExecutor(final ServerAddress serverAddress, final Supplier<ExecutorService> workManagerSource) {
    executorPerServerAddress.put(serverAddress, workManagerSource);
  }

  public void removeExecutor(ServerAddress serverAddress) {
    executorPerServerAddress.remove(serverAddress);
  }

  @Override
  public Executor getExecutor(ServerAddress serverAddress) {
    return executorPerServerAddress.get(serverAddress).get();
  }
}
